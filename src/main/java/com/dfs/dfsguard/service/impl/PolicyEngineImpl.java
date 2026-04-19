package com.dfs.dfsguard.service.impl;

import com.dfs.dfsguard.model.PolicyDecision;
import com.dfs.dfsguard.model.ToolProposal;
import com.dfs.dfsguard.service.PolicyEngine;
import com.dfs.dfsguard.tool.ToolMetadata;
import com.dfs.dfsguard.tool.ToolRegistry;
import com.dfs.dfsguard.tool.ToolRiskLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PolicyEngineImpl implements PolicyEngine {

    private final ToolRegistry toolRegistry;

    public PolicyEngineImpl(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    @Override
    public PolicyDecision evaluate(
            String userRole,
            ToolProposal toolProposal,
            List<String> promptHits,
            boolean retrievedInstructionFlag
    ) {
        if (toolProposal == null || toolProposal.name() == null || toolProposal.name().isBlank()) {
            return new PolicyDecision(false, "Missing tool proposal", false);
        }

        if (promptHits != null && !promptHits.isEmpty()) {
            return new PolicyDecision(false, "Blocked: prompt injection detected", false);
        }

        if (retrievedInstructionFlag) {
            return new PolicyDecision(false, "Blocked: action influenced by untrusted retrieved content", false);
        }

        Optional<ToolMetadata> metaOpt = toolRegistry.findMetadata(toolProposal.name());
        if (metaOpt.isEmpty()) {
            return new PolicyDecision(false, "Blocked: unknown tool " + toolProposal.name(), false);
        }

        ToolMetadata meta = metaOpt.get();
        String normalizedRole = (userRole == null ? "" : userRole).trim().toLowerCase(Locale.ROOT);
        if (!meta.allowedRoles().contains(normalizedRole)) {
            return new PolicyDecision(false, "Blocked: role not allowed for tool " + meta.name(), false);
        }

        boolean isHighRisk = meta.riskLevel() == ToolRiskLevel.HIGH;
        boolean requiresApproval = meta.requiresApproval();

        if (isHighRisk && requiresApproval) {
            // Not allowed *yet* (until approval is granted by ApprovalService in a later phase)
            return new PolicyDecision(false, "Approval required for tool " + meta.name(), true);
        }

        return new PolicyDecision(true, "Allowed", false);
    }
}


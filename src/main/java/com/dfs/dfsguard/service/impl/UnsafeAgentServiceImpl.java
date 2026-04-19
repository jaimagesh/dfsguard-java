package com.dfs.dfsguard.service.impl;

import com.dfs.dfsguard.model.ChatRequest;
import com.dfs.dfsguard.model.ChatResponse;
import com.dfs.dfsguard.service.UnsafeAgentService;
import com.dfs.dfsguard.tool.CloseCaseTool;
import com.dfs.dfsguard.tool.SearchDocsTool;
import com.dfs.dfsguard.tool.SendEmailTool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UnsafeAgentServiceImpl implements UnsafeAgentService {

    private static final Pattern CASE_ID = Pattern.compile("(CASE-\\d+)", Pattern.CASE_INSENSITIVE);

    private final SearchDocsTool searchDocsTool;
    private final SendEmailTool sendEmailTool;
    private final CloseCaseTool closeCaseTool;

    public UnsafeAgentServiceImpl(
            SearchDocsTool searchDocsTool,
            SendEmailTool sendEmailTool,
            CloseCaseTool closeCaseTool
    ) {
        this.searchDocsTool = searchDocsTool;
        this.sendEmailTool = sendEmailTool;
        this.closeCaseTool = closeCaseTool;
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        String msg = request.message() == null ? "" : request.message();
        String msgLower = msg.toLowerCase();

        Map<String, Object> toolResult;
        if (msgLower.contains("email")) {
            toolResult = sendEmailTool.execute(Map.of(
                    "to", "demo@example.com",
                    "subject", "DfsGuard demo",
                    "body", msg
            ));
        } else if (msgLower.contains("close case")) {
            toolResult = closeCaseTool.execute(Map.of(
                    "caseId", extractCaseIdOrDefault(msg)
            ));
        } else if (msgLower.contains("document")) {
            toolResult = searchDocsTool.execute(Map.of(
                    "query", msg
            ));
        } else {
            toolResult = Map.of(
                    "status", "no_tool_selected"
            );
        }

        return new ChatResponse(
                "unsafe",
                "Unsafe mode: executed tool directly",
                toolResult,
                List.of(),
                false
        );
    }

    private static String extractCaseIdOrDefault(String msg) {
        Matcher m = CASE_ID.matcher(msg);
        if (m.find()) {
            return m.group(1).toUpperCase();
        }
        return "CASE-1001";
    }
}


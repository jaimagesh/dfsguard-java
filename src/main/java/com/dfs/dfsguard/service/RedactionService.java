package com.dfs.dfsguard.service;

import java.util.Map;

public interface RedactionService {
    String redactMessage(String message);

    Map<String, Object> redactToolResult(Map<String, Object> toolResult);
}

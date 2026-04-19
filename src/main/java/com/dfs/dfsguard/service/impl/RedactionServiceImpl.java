package com.dfs.dfsguard.service.impl;

import com.dfs.dfsguard.service.RedactionService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedactionServiceImpl implements RedactionService {

    @Override
    public String redactMessage(String message) {
        return message;
    }

    @Override
    public Map<String, Object> redactToolResult(Map<String, Object> toolResult) {
        return toolResult;
    }
}


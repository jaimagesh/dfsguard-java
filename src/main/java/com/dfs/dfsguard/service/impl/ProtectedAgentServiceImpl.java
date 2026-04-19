package com.dfs.dfsguard.service.impl;

import com.dfs.dfsguard.model.ChatRequest;
import com.dfs.dfsguard.model.ChatResponse;
import com.dfs.dfsguard.service.ProtectedAgentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProtectedAgentServiceImpl implements ProtectedAgentService {

    @Override
    public ChatResponse chat(ChatRequest request) {
        return new ChatResponse(
                "protected",
                "Protected mode not implemented yet",
                null,
                List.of(),
                false
        );
    }
}


package com.dfs.dfsguard.service;

import com.dfs.dfsguard.model.ChatRequest;
import com.dfs.dfsguard.model.ChatResponse;

public interface UnsafeAgentService {
    ChatResponse chat(ChatRequest request);
}

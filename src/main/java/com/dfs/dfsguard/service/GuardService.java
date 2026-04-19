package com.dfs.dfsguard.service;

import java.util.List;

public interface GuardService {
    List<String> detectPromptInjectionHits(String text);

    List<String> detectRetrievedInstructionHits(String retrievedText);
}

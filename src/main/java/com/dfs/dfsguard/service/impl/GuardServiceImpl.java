package com.dfs.dfsguard.service.impl;

import com.dfs.dfsguard.service.GuardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class GuardServiceImpl implements GuardService {

    private static final List<String> PROMPT_INJECTION_PATTERNS = List.of(
            "ignore previous instructions",
            "reveal system prompt",
            "act as admin",
            "send to attacker"
    );

    @Override
    public List<String> detectPromptInjectionHits(String text) {
        return findHits(text, PROMPT_INJECTION_PATTERNS);
    }

    @Override
    public List<String> detectRetrievedInstructionHits(String retrievedText) {
        return findHits(retrievedText, PROMPT_INJECTION_PATTERNS);
    }

    private static List<String> findHits(String text, List<String> patterns) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String haystack = text.toLowerCase(Locale.ROOT);
        Set<String> hits = new LinkedHashSet<>();
        for (String p : patterns) {
            if (haystack.contains(p)) {
                hits.add(p);
            }
        }
        return new ArrayList<>(hits);
    }
}


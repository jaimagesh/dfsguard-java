package com.dfs.dfsguard.model;

import java.util.List;
import java.util.Map;

public record ChatResponse(
        String mode,
        String message,
        Map<String, Object> toolResult,
        List<String> promptHits,
        boolean retrievedInstructionFlag
) {
}

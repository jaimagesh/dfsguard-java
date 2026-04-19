package com.dfs.dfsguard.tool;

import java.util.Set;

public record ToolMetadata(
        String name,
        ToolRiskLevel riskLevel,
        Set<String> allowedRoles,
        boolean requiresApproval
) {
}


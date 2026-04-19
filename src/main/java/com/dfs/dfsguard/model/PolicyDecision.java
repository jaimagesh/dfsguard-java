package com.dfs.dfsguard.model;

public record PolicyDecision(boolean allowed, String reason, boolean requiresApproval) {
}

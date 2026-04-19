package com.dfs.dfsguard.model;

import java.time.Instant;
import java.util.Map;

public record AuditEvent(Instant timestamp, String userId, String action, Map<String, Object> detail) {
}

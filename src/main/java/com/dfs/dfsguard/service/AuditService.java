package com.dfs.dfsguard.service;

import com.dfs.dfsguard.model.AuditEvent;

public interface AuditService {
    void log(AuditEvent event);
}

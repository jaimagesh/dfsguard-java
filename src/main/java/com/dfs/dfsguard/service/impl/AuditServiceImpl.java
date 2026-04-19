package com.dfs.dfsguard.service.impl;

import com.dfs.dfsguard.model.AuditEvent;
import com.dfs.dfsguard.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditServiceImpl.class);

    @Override
    public void log(AuditEvent event) {
        // Demo-friendly: log as structured-ish message.
        log.info("AUDIT userId={} action={} detail={}", event.userId(), event.action(), event.detail());
    }
}


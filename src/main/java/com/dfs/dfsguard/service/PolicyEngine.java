package com.dfs.dfsguard.service;

import com.dfs.dfsguard.model.PolicyDecision;
import com.dfs.dfsguard.model.ToolProposal;

import java.util.List;

public interface PolicyEngine {
    PolicyDecision evaluate(
            String userRole,
            ToolProposal toolProposal,
            List<String> promptHits,
            boolean retrievedInstructionFlag
    );
}

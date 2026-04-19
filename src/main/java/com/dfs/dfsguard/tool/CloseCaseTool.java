package com.dfs.dfsguard.tool;

import com.dfs.dfsguard.model.Case;
import com.dfs.dfsguard.repository.CaseRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class CloseCaseTool implements Tool {

    private final CaseRepository caseRepository;

    public CloseCaseTool(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @Override
    public String name() {
        return "CloseCaseTool";
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> arguments) {
        String caseId = String.valueOf(arguments.getOrDefault("caseId", "")).trim();

        Optional<Case> updated = caseRepository.closeCase(caseId);
        if (updated.isEmpty()) {
            return Map.of(
                    "status", "not_found",
                    "caseId", caseId
            );
        }

        return Map.of(
                "status", "closed",
                "case", updated.get()
        );
    }
}


package com.dfs.dfsguard.model;

public record Case(
        String caseId,
        String patientName,
        String status,
        String notes
) {
}


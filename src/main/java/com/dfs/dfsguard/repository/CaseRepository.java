package com.dfs.dfsguard.repository;

import com.dfs.dfsguard.model.Case;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CaseRepository {

    private static final String CASES_PATH = "data/cases.json";

    private final Map<String, Case> casesById;

    public CaseRepository(ObjectMapper objectMapper) {
        try (InputStream in = new ClassPathResource(CASES_PATH).getInputStream()) {
            List<Case> loaded = objectMapper.readValue(in, new TypeReference<>() {});
            this.casesById = new ConcurrentHashMap<>();
            for (Case c : loaded) {
                if (c.caseId() != null) {
                    this.casesById.put(c.caseId(), c);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + CASES_PATH + " from classpath", e);
        }
    }

    public List<Case> findAll() {
        return new ArrayList<>(casesById.values());
    }

    public Optional<Case> findByCaseId(String caseId) {
        return Optional.ofNullable(casesById.get(caseId));
    }

    public Optional<Case> closeCase(String caseId) {
        return Optional.ofNullable(casesById.computeIfPresent(caseId, (ignored, existing) -> new Case(
                existing.caseId(),
                existing.patientName(),
                "resolved",
                existing.notes()
        )));
    }
}


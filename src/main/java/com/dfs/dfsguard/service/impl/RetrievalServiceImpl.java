package com.dfs.dfsguard.service.impl;

import com.dfs.dfsguard.model.RetrievalChunk;
import com.dfs.dfsguard.service.GuardService;
import com.dfs.dfsguard.service.RetrievalService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class RetrievalServiceImpl implements RetrievalService {

    private static final String DOCS_GLOB = "classpath:data/docs/*.txt";

    private final GuardService guardService;

    public RetrievalServiceImpl(GuardService guardService) {
        this.guardService = guardService;
    }

    @Override
    public List<RetrievalChunk> retrieve(String query) {
        List<RetrievalChunk> chunks = new ArrayList<>();

        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(DOCS_GLOB);
            for (Resource r : resources) {
                String text = readUtf8(r);
                boolean containsInstructions = !guardService.detectRetrievedInstructionHits(text).isEmpty();
                chunks.add(new RetrievalChunk(
                        text,
                        r.getFilename(),
                        "untrusted",
                        containsInstructions
                ));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to retrieve docs from " + DOCS_GLOB, e);
        }

        return chunks;
    }

    private static String readUtf8(Resource resource) throws IOException {
        try (InputStream in = resource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}


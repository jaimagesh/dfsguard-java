package com.dfs.dfsguard.tool;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchDocsTool implements Tool {

    private static final String DOCS_GLOB = "classpath:data/docs/*.txt";

    @Override
    public String name() {
        return "SearchDocsTool";
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> arguments) {
        String query = String.valueOf(arguments.getOrDefault("query", "")).trim();

        List<Map<String, Object>> hits = new ArrayList<>();
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(DOCS_GLOB);
            for (Resource r : resources) {
                String text = readUtf8(r);
                if (query.isEmpty() || text.toLowerCase().contains(query.toLowerCase())) {
                    Map<String, Object> hit = new LinkedHashMap<>();
                    hit.put("source", r.getFilename());
                    hit.put("text", text);
                    hits.add(hit);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to search docs from " + DOCS_GLOB, e);
        }

        return Map.of(
                "query", query,
                "results", hits
        );
    }

    private static String readUtf8(Resource resource) throws IOException {
        try (InputStream in = resource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}


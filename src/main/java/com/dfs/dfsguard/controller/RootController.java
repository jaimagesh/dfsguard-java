package com.dfs.dfsguard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "status", "ok",
                "swaggerUi", "/swagger-ui/index.html",
                "health", "/api/health",
                "chat", "/api/chat"
        );
    }
}


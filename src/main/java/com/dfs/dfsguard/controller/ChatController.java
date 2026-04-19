package com.dfs.dfsguard.controller;

import com.dfs.dfsguard.model.ChatRequest;
import com.dfs.dfsguard.model.ChatResponse;
import com.dfs.dfsguard.service.ProtectedAgentService;
import com.dfs.dfsguard.service.UnsafeAgentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final UnsafeAgentService unsafeAgentService;
    private final ProtectedAgentService protectedAgentService;

    public ChatController(
            UnsafeAgentService unsafeAgentService,
            ProtectedAgentService protectedAgentService
    ) {
        this.unsafeAgentService = unsafeAgentService;
        this.protectedAgentService = protectedAgentService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        return switch (request.mode().toLowerCase()) {
            case "unsafe" -> unsafeAgentService.chat(request);
            case "protected" -> protectedAgentService.chat(request);
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported mode: " + request.mode()
            );
        };
    }

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}

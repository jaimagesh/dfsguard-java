package com.dfs.dfsguard.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank String userId,
        @NotBlank String mode,
        @NotBlank String message
) {
}

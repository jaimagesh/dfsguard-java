package com.dfs.dfsguard.tool;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ToolRegistry {

    private final Map<String, Tool> toolsByName;
    private final Map<String, ToolMetadata> metadataByName;

    public ToolRegistry(List<Tool> tools) {
        this.toolsByName = tools.stream()
                .collect(Collectors.toUnmodifiableMap(Tool::name, Function.identity()));

        this.metadataByName = Map.of(
                "SearchDocsTool", new ToolMetadata(
                        "SearchDocsTool",
                        ToolRiskLevel.LOW,
                        Set.of("operator", "admin"),
                        false
                ),
                "SendEmailTool", new ToolMetadata(
                        "SendEmailTool",
                        ToolRiskLevel.HIGH,
                        Set.of("operator", "admin"),
                        true
                ),
                "CloseCaseTool", new ToolMetadata(
                        "CloseCaseTool",
                        ToolRiskLevel.HIGH,
                        Set.of("admin"),
                        true
                )
        );
    }

    public Optional<Tool> findTool(String name) {
        return Optional.ofNullable(toolsByName.get(name));
    }

    public Optional<ToolMetadata> findMetadata(String name) {
        return Optional.ofNullable(metadataByName.get(name));
    }

    public Map<String, ToolMetadata> allMetadata() {
        return metadataByName;
    }
}


package com.dfs.dfsguard.tool;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SendEmailTool implements Tool {

    @Override
    public String name() {
        return "SendEmailTool";
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> arguments) {
        String to = String.valueOf(arguments.getOrDefault("to", "")).trim();
        String subject = String.valueOf(arguments.getOrDefault("subject", "")).trim();
        String body = String.valueOf(arguments.getOrDefault("body", "")).trim();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "mock_sent");
        result.put("to", to);
        result.put("subject", subject);
        result.put("bodyPreview", body.length() <= 80 ? body : body.substring(0, 80) + "...");
        return result;
    }
}


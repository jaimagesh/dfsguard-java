package com.dfs.dfsguard.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public Map<String, Object> error(HttpServletRequest request) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        return Map.of(
                "status", status == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : status,
                "message", "No handler for this path",
                "swaggerUi", "/swagger-ui/index.html"
        );
    }
}


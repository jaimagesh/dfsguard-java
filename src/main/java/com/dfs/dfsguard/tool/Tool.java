package com.dfs.dfsguard.tool;

import java.util.Map;

public interface Tool {
    String name();

    Map<String, Object> execute(Map<String, Object> arguments);
}


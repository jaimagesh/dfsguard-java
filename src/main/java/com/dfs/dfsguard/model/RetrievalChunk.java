package com.dfs.dfsguard.model;

public record RetrievalChunk(String text, String source, String trust, boolean containsInstructions) {
}

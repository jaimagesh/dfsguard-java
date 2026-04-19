package com.dfs.dfsguard.service;

import com.dfs.dfsguard.model.RetrievalChunk;

import java.util.List;

public interface RetrievalService {
    List<RetrievalChunk> retrieve(String query);
}

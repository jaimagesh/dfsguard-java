package com.dfs.dfsguard.repository;

import com.dfs.dfsguard.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private static final String USERS_PATH = "data/users.json";

    private final List<User> users;

    public UserRepository(ObjectMapper objectMapper) {
        try (InputStream in = new ClassPathResource(USERS_PATH).getInputStream()) {
            this.users = objectMapper.readValue(in, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + USERS_PATH + " from classpath", e);
        }
    }

    public List<User> findAll() {
        return users;
    }

    public Optional<User> findByUserId(String userId) {
        return users.stream()
                .filter(u -> u.userId() != null && u.userId().equals(userId))
                .findFirst();
    }
}


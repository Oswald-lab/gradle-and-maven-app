package com.example.app;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, User> usersById = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        save("demo@example.com", "Password123!");
    }

    public void save(String email, String rawPassword) {
        String normalizedEmail = normalize(email);
        String id = UUID.randomUUID().toString();
        User user = new User(id, normalizedEmail, PasswordHasher.hash(rawPassword));
        users.put(normalizedEmail, user);
        usersById.put(id, user);
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(normalize(email)));
    }

    public Optional<User> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(usersById.get(id));
    }

    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

package com.example.app;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        save("demo@example.com", "Password123!");
    }

    public void save(String email, String rawPassword) {
        String normalizedEmail = normalize(email);
        users.put(normalizedEmail, new User(normalizedEmail, PasswordHasher.hash(rawPassword)));
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(normalize(email)));
    }

    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

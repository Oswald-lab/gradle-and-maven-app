package com.example.app;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentMap<String, UserAccount> users = new ConcurrentHashMap<>();

    public InMemoryUserRepository() {
        UserAccount demo = UserAccount.withPassword("demo@example.com", "Password123!");
        users.put(demo.email(), demo);
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    @Override
    public void save(UserAccount user) {
        users.put(user.email(), user);
    }
}

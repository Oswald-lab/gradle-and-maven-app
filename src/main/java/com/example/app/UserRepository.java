package com.example.app;

import java.util.Optional;

public interface UserRepository {
    Optional<UserAccount> findByEmail(String email);

    void save(UserAccount user);
}

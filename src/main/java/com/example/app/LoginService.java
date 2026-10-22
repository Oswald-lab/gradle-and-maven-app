package com.example.app;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final InMemoryUserRepository userRepository;

    public LoginService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password is required");
        }

        return userRepository.findByEmail(email)
                .filter(user -> PasswordHasher.matches(password, user.passwordHash()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }
}

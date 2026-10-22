package com.example.app;

public record UserAccount(String email, String salt, String passwordHash) {

    public static UserAccount withPassword(String email, String password) {
        String salt = PasswordHasher.newSalt();
        return new UserAccount(email, salt, PasswordHasher.hash(password, salt));
    }
}

package com.example.app;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

final class PasswordHasher {
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    static String newSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return toHex(salt);
    }

    static String hash(String rawPassword) {
        return toHex(digest(rawPassword.getBytes(StandardCharsets.UTF_8)));
    }

    static String hash(String rawPassword, String salt) {
        return toHex(digest((salt + rawPassword).getBytes(StandardCharsets.UTF_8)));
    }

    static boolean matches(String rawPassword, String passwordHash) {
        return hash(rawPassword).equalsIgnoreCase(passwordHash);
    }

    private static byte[] digest(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            builder.append(String.format("%02x", value));
        }
        return builder.toString();
    }
}

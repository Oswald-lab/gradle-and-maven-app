package com.example.app;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

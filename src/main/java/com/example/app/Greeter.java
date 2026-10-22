package com.example.app;

public class Greeter {
    public String greet(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        return "Hello, " + name + "!";
    }
}

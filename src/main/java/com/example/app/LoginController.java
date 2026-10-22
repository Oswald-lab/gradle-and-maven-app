package com.example.app;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody(required = false) LoginRequest request) {
        String email = request == null ? null : request.email();
        String password = request == null ? null : request.password();

        User user = loginService.login(email, password);

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "email", user.email()));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> authLogin(@RequestBody(required = false) LoginRequest request) {
        String email = request == null ? null : request.email();
        String password = request == null ? null : request.password();

        User user = loginService.login(email, password);

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "email", user.email()));
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, String>> getUser(@RequestParam(required = false) String email) {
        return loginService.findByEmail(email)
                .map(user -> ResponseEntity.ok(Map.of("email", user.email())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found")));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidation(ValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", exception.getMessage()));
    }

    public record LoginRequest(String email, String password) {
    }
}

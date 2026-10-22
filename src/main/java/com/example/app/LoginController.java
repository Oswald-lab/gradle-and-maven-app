package com.example.app;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping({"/", "/login"})
    public ResponseEntity<Void> loginPage() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/login.html")
                .build();
    }

    @PostMapping({"/api/login", "/api/auth/login"})
    public ResponseEntity<Map<String, Object>> login(@RequestBody(required = false) LoginRequest request,
                                                     HttpServletRequest httpRequest) {
        LoginRequest credentials = request != null ? request : new LoginRequest(null, null);

        try {
            LoginService.LoginResult result = loginService.login(credentials.email(), credentials.password());
            httpRequest.getSession(true).setAttribute("userEmail", result.email());

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("message", "Login successful");
            body.put("email", result.email());
            body.put("redirectUrl", "/home.html");
            return ResponseEntity.ok(body);
        } catch (ValidationException e) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "Validation failed");
            body.put("fieldErrors", e.getFieldErrors());
            return ResponseEntity.badRequest().body(body);
        } catch (InvalidCredentialsException e) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
    }
}

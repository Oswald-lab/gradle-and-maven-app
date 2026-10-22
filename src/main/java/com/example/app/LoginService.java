package com.example.app;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResult login(String email, String password) {
        Map<String, String> fieldErrors = validate(email, password);
        if (!fieldErrors.isEmpty()) {
            throw new ValidationException(fieldErrors);
        }

        String normalizedEmail = normalize(email);
        Optional<UserAccount> account = userRepository.findByEmail(normalizedEmail);
        if (account.isEmpty() || !PasswordHasher.verify(password, account.get().salt(), account.get().passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return new LoginResult(account.get().email());
    }

    public UserAccount register(String email, String password) {
        Map<String, String> fieldErrors = validate(email, password);
        if (!fieldErrors.isEmpty()) {
            throw new ValidationException(fieldErrors);
        }

        String normalizedEmail = normalize(email);
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            Map<String, String> errors = new LinkedHashMap<>();
            errors.put("email", "An account with this email already exists");
            throw new ValidationException(errors);
        }

        UserAccount account = UserAccount.withPassword(normalizedEmail, password);
        userRepository.save(account);
        return account;
    }

    private Map<String, String> validate(String email, String password) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        if (email == null || email.isBlank()) {
            fieldErrors.put("email", "Email is required");
        } else if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            fieldErrors.put("email", "Enter a valid email address");
        }

        if (password == null || password.isBlank()) {
            fieldErrors.put("password", "Password is required");
        }

        return fieldErrors;
    }

    private String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public record LoginResult(String email) {
    }
}

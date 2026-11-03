package com.vitngon.vitngon_backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public record RegisterRequest(
            String fullName,
            String username,
            String password,
            String phone,
            String email
    ) {}

    public record LoginRequest(
            String username,
            String password
    ) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        if (request.fullName() == null
                || request.username() == null
                || request.password() == null
                || request.fullName().isBlank()
                || request.username().isBlank()
                || request.password().length() < 8) {

            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "Hãy nhập họ tên, tên đăng nhập và mật khẩu từ 8 ký tự."));
        }

        String username = request.username().trim();

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("message", "Tên đăng nhập đã tồn tại."));
        }

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setUsername(username);
        user.setPasswordHash(
                passwordEncoder.encode(request.password()));
        user.setPhone(request.phone());
        user.setEmail(request.email());
        user.setRole("USER");
        user.setActive(true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("message",
                        "Đăng ký thành công. Bạn có thể đăng nhập."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        if (request.username() == null
                || request.password() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "Vui lòng nhập tên đăng nhập và mật khẩu."));
        }

        User user = userRepository
                .findByUsername(request.username().trim())
                .orElse(null);

        if (user == null
                || !user.isActive()
                || !passwordEncoder.matches(
                request.password(), user.getPasswordHash())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message",
                            "Tên đăng nhập hoặc mật khẩu không đúng."));
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Đăng nhập thành công.");
        result.put("id", user.getId());
        result.put("fullName", user.getFullName());
        result.put("username", user.getUsername());
        result.put("role", user.getRole());

        return ResponseEntity.ok(result);
    }
}

package com.vitngon.vitngon_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminAccountInitializer {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository) {
        return args -> {
            String username = "admin";

            if (userRepository.existsByUsername(username)) {
                System.out.println(
                        "Tai khoan Admin da ton tai, khong tao lai.");
                return;
            }

            User admin = new User();
            admin.setFullName("Quan tri vien VIT NGON");
            admin.setUsername(username);
            admin.setPasswordHash(
                    new BCryptPasswordEncoder().encode(
                            "VitNgonAdmin@2026"));
            admin.setRole("ADMIN");
            admin.setActive(true);

            userRepository.save(admin);

            System.out.println(
                    "Da tao tai khoan Admin VIT NGON.");
        };
    }
}
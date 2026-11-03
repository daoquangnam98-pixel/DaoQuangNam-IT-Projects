
package com.vitngon.vitngon_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class ResetAdminPassword implements CommandLineRunner {

    private final UserRepository userRepository;

    public ResetAdminPassword(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        User admin = userRepository.findByUsername("admin")
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy tài khoản admin"));

        admin.setPasswordHash(
                new BCryptPasswordEncoder().encode("VitNgonAdmin@2026"));

        admin.setRole("ADMIN");
        admin.setActive(true);
        userRepository.save(admin);

        System.out.println("DA DAT LAI MAT KHAU ADMIN THANH CONG");
    }
}

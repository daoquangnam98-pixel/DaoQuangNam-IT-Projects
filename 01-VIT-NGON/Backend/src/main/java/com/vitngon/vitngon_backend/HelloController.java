package com.vitngon.vitngon_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "VIT NGON BACKEND DANG HOAT DONG!";
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Ket noi API VIT NGON thanh cong!";
    }
}
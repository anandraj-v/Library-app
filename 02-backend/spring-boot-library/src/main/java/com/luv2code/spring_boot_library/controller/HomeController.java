package com.luv2code.spring_boot_library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	@GetMapping("/")
    public String home() {
        return "Welcome to the Library API";
    }
}

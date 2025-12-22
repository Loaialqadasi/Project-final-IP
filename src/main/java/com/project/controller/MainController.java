package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // This handles "localhost:8080/dashboard"
    @GetMapping("/dashboard")
    public String showDashboard() {
        // This looks for src/main/resources/templates/student/dashboard.html
        return "student/dashboard";
    }
}
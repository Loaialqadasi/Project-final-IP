package com.project.controller;

import com.project.util.DataStore;
import com.project.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {

        // 1. Check for Admin
        if (username.equalsIgnoreCase("admin") && password.equals("admin")) {
            session.setAttribute("user", "Admin");
            session.setAttribute("role", "Admin");
            return "redirect:/admin/dashboard";
        }

        // 2. Logic for Students
        if (username.equals(password)) {
            // Check if user exists in DataStore
            boolean exists = DataStore.users.stream().anyMatch(u -> u.getName().equalsIgnoreCase(username));

            if (!exists) {
                String newId = String.valueOf(100 + DataStore.users.size() + 1);
                DataStore.users.add(new User(newId, username, "Student", "Active"));
            }

            session.setAttribute("user", username);
            session.setAttribute("role", "Student");
            return "redirect:/student/dashboard";
        }

        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
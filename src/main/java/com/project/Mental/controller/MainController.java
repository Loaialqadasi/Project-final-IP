<<<<<<< HEAD
package com.project.Mental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // ===== MAIN PAGES =====
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ===== STUDENT ROUTES =====
    @GetMapping("/dashboard")
    public String showStudentDashboard() {
        return "student/dashboard";
    }

    @GetMapping("/booking")
    public String showBooking() {
        return "student/booking";
    }

    @GetMapping("/library")
    public String showLibrary() {
        return "student/library";
    }

    @GetMapping("/chatbot")
    public String showChatbot() {
        return "student/chatbot";
    }

    @GetMapping("/tracker")
    public String showTracker() {
        return "student/tracker";
    }

    // ===== COUNSELOR ROUTES =====
    @GetMapping("/counselor/dashboard")
    public String showCounselorDashboard() {
        return "counselor/dashboard";
    }

    @GetMapping("/counselor/session-notes")
    public String showSessionNotes() {
        return "counselor/session_notes";
    }

    // ===== ADMIN ROUTES =====
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/users")
    public String showUserList() {
        return "admin/user_list";
    }

    // ===== AUTH ROUTES =====
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
=======
package com.project.Mental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // This handles requests to "localhost:8080/" (The landing page)
    @GetMapping("/")
    public String index() {
        return "index"; // You might need an index.html later
    }

    // This handles "localhost:8080/dashboard"
    @GetMapping("/dashboard")
    public String showDashboard() {
        // This looks for src/main/resources/templates/student/dashboard.html
        return "student/dashboard";
    }
>>>>>>> 6f0699dff822453932d99aa7519157956dace76b
}
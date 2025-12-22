package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.model.User;
import com.project.util.DataStore;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Note: For testing easier, the strict Admin check is commented.
        // If you want strict security, uncomment the next line:
        // String role = (String) session.getAttribute("role");
        // if (role == null || !role.equals("Admin")) return "redirect:/login";

        // 1. KPI Stats
        model.addAttribute("totalUsers", DataStore.users.size());
        long activeUsers = DataStore.users.stream().filter(u -> "Active".equals(u.getStatus())).count();
        model.addAttribute("activeUsers", activeUsers);

        // 2. Chart Data: Mood Distribution
        long happy = DataStore.moods.stream().filter(m -> m.getMoodLevel() >= 4).count();
        long neutral = DataStore.moods.stream().filter(m -> m.getMoodLevel() == 3).count();
        long sad = DataStore.moods.stream().filter(m -> m.getMoodLevel() <= 2).count();

        model.addAttribute("happyCount", happy);
        model.addAttribute("neutralCount", neutral);
        model.addAttribute("sadCount", sad);

        // 3. Chart Data
        model.addAttribute("userCount", DataStore.users.size());

        return "admin/dashboard";
    }

    @GetMapping("/admin/users")
    public String showUserList(HttpSession session, Model model) {
        model.addAttribute("users", DataStore.users);
        return "admin/user_list";
    }

    @PostMapping("/admin/user/add")
    public String addUser(@RequestParam String name, @RequestParam String role) {
        String newId = String.valueOf(100 + DataStore.users.size() + 1);
        DataStore.users.add(new User(newId, name, role, "Active"));
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/ban")
    public String banUser(@RequestParam String userId) {
        for (User u : DataStore.users) {
            if (u.getUserId().equals(userId)) {
                u.setStatus("Banned");
                break;
            }
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/unban")
    public String unbanUser(@RequestParam String userId) {
        for (User u : DataStore.users) {
            if (u.getUserId().equals(userId)) {
                u.setStatus("Active");
                break;
            }
        }
        return "redirect:/admin/users";
    }
}
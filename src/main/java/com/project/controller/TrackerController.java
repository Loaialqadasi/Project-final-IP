package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.model.MoodEntry;
import com.project.util.DataStore;

import jakarta.servlet.http.HttpSession;

@Controller
public class TrackerController {

    @GetMapping("/student/dashboard")
    public String showStudentDashboard(HttpSession session, Model model) {
        // Security Check
        String user = (String) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        model.addAttribute("username", user);
        return "student/dashboard";
    }

    @GetMapping("/student/tracker")
    public String showTracker(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";

        model.addAttribute("entries", DataStore.moods);
        return "student/tracker";
    }

    @PostMapping("/student/tracker/add")
    public String addMood(@RequestParam int moodLevel, @RequestParam String note) {
        String today = java.time.LocalDate.now().toString();
        // Add to Global DataStore
        DataStore.moods.add(new MoodEntry(note, moodLevel, today));
        return "redirect:/student/tracker";
    }
}
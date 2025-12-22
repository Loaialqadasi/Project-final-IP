package com.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CounselorController {

    // --- TEMPORARY MEMORY FOR DEMO ---
    // This variable will hold the note text while the server is running.
    private String savedNote = "Student needs more self depends and less stress in his daily life.";
    private String savedDate = "2025-05-05";

    @GetMapping("/counselor/dashboard")
    public String dashboard(Model model) {
        // 1. Mock Student Data
        List<Map<String, Object>> students = new ArrayList<>();
        students.add(Map.of("id", 1, "name", "KHALID", "matric", "A23CS0001", "status", "High Risk", "score", 18));
        students.add(Map.of("id", 2, "name", "Youssef", "matric", "A23CS0002", "status", "Attention Needed", "score", 12));
        students.add(Map.of("id", 3, "name", "Ahmed", "matric", "A23CS0003", "status", "Stable", "score", 5));
        students.add(Map.of("id", 4, "name", "Salem", "matric", "A23CS0004", "status", "Stable", "score", 2));

        model.addAttribute("counselorName", "Loai");
        model.addAttribute("students", students);
        
        // 2. Send the SAVED DATA to the HTML
        model.addAttribute("currentNote", savedNote);
        model.addAttribute("currentDate", savedDate);
        
        return "counselor/dashboard";
    }

    @PostMapping("/counselor/note/save")
    public String saveNote(@RequestParam String noteContent, 
                           @RequestParam String date,
                           Model model) {
        
        // 3. Update the Memory with what you typed
        this.savedNote = noteContent;
        this.savedDate = date;
        
        System.out.println("Updated Note: " + this.savedNote);

        return "redirect:/counselor/dashboard"; 
    }

    @GetMapping("/counselor/appointments")
    public String appointments() {
        return "counselor/appointments";
    }

    @GetMapping("/counselor/profile")
    public String profile() {
        return "counselor/profile";
    }

    @GetMapping("/counselor/forum")
    public String forum() {
        return "counselor/forum";
    }

    @GetMapping("/counselor/students")
    public String students(Model model) {
        List<Map<String, Object>> students = new ArrayList<>();
        students.add(Map.of("id", 1, "name", "KHALID", "matric", "A23CS0001", "status", "High Risk"));
        students.add(Map.of("id", 2, "name", "Youssef", "matric", "A23CS0002", "status", "Attention Needed"));
        students.add(Map.of("id", 3, "name", "Ahmed", "matric", "A23CS0003", "status", "Stable"));
        students.add(Map.of("id", 4, "name", "Salem", "matric", "A23CS0004", "status", "Stable"));
        
        model.addAttribute("students", students);
        return "counselor/students";
    }

    @GetMapping("/counselor/settings")
    public String settings() {
        return "counselor/settings";
    }
}
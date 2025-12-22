package com.project.Mental.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AssessmentController {

    @GetMapping("/assessment")
    public String showAssessmentForm() {
        return "student/assessment_form";
    }

    @PostMapping("/assessment/submit")
    public String submitAssessment(
            // --- 9 REQUIRED QUESTIONS (PHQ-9) ---
            @RequestParam(name = "q1", defaultValue = "0") int q1,
            @RequestParam(name = "q2", defaultValue = "0") int q2,
            @RequestParam(name = "q3", defaultValue = "0") int q3,
            @RequestParam(name = "q4", defaultValue = "0") int q4,
            @RequestParam(name = "q5", defaultValue = "0") int q5,
            @RequestParam(name = "q6", defaultValue = "0") int q6,
            @RequestParam(name = "q7", defaultValue = "0") int q7,
            @RequestParam(name = "q8", defaultValue = "0") int q8,
            @RequestParam(name = "q9", defaultValue = "0") int q9,
            
            // --- 9 OPTIONAL QUESTIONS (Extra Anxiety/Stress Check) ---
            // defaultValue="0" ensures they don't break the app if skipped
            @RequestParam(name = "opt1", defaultValue = "0") int opt1,
            @RequestParam(name = "opt2", defaultValue = "0") int opt2,
            @RequestParam(name = "opt3", defaultValue = "0") int opt3,
            @RequestParam(name = "opt4", defaultValue = "0") int opt4,
            @RequestParam(name = "opt5", defaultValue = "0") int opt5,
            @RequestParam(name = "opt6", defaultValue = "0") int opt6,
            @RequestParam(name = "opt7", defaultValue = "0") int opt7,
            @RequestParam(name = "opt8", defaultValue = "0") int opt8,
            @RequestParam(name = "opt9", defaultValue = "0") int opt9,
            Model model) {

        // 1. Calculate Base Score (Required)
        int baseScore = q1 + q2 + q3 + q4 + q5 + q6 + q7 + q8 + q9;
        
        // 2. Calculate Optional Score
        int extraScore = opt1 + opt2 + opt3 + opt4 + opt5 + opt6 + opt7 + opt8 + opt9;

        // 3. Total Score
        int totalScore = baseScore + extraScore;
        
        // 4. Determine Max Possible Score for the circular graph
        // If they answered extra questions, the '100%' circle needs to be bigger
        int maxPossible = 27; // Base max
        if (extraScore > 0) {
            maxPossible = 54; // Base (27) + Extra (27)
        }

        // 5. Determine Status (Adjusted for total score)
        String status;
        String feedbackText;
        String badgeClass;

        // Simple logic: We divide score by maxPossible to get a percentage severity
        double percentage = (double) totalScore / maxPossible;

        if (percentage <= 0.15) { // 0-15%
            status = "Low Stress";
            feedbackText = "You seem to be handling things well. Your responses indicate minimal stress levels.";
            badgeClass = "bg-success-subtle text-success";
        } else if (percentage <= 0.35) { // 16-35%
            status = "Mild Stress";
            feedbackText = "You have some mild symptoms. It's common to feel this way during busy times.";
            badgeClass = "bg-warning-subtle text-warning";
        } else if (percentage <= 0.60) { // 36-60%
            status = "Moderate Stress";
            feedbackText = "Your stress levels are moderate. The extra questions you answered highlight some anxiety.";
            badgeClass = "bg-warning text-dark";
        } else { // 60%+
            status = "High Stress";
            feedbackText = "Your responses indicate high stress or anxiety. Please reach out to a counselor.";
            badgeClass = "bg-danger text-white";
        }

        String downloadData = "Total Score: " + totalScore + "/" + maxPossible + " | Result: " + status;

        model.addAttribute("score", totalScore);
        model.addAttribute("maxScore", maxPossible); // Pass this to HTML for the " / 27" text
        model.addAttribute("status", status);
        model.addAttribute("feedbackText", feedbackText);
        model.addAttribute("badgeClass", badgeClass);
        model.addAttribute("name", "Jane Doe");
        model.addAttribute("date", java.time.LocalDate.now());
        model.addAttribute("downloadData", downloadData);

        return "student/assessment_result";
    }

    @GetMapping("/assessment/download")
    public ResponseEntity<String> downloadReport(@RequestParam("data") String data) {
        String fileContent = "MINDWELL ASSESSMENT REPORT\n==========================\n" + data;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"My_Report.txt\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileContent);
    }
}
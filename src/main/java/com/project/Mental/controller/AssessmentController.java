package com.project.Mental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.Mental.dao.AssessmentResultDAO;
import com.project.Mental.model.AssessmentResult;
import com.project.Mental.service.AssessmentService;

@Controller
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private AssessmentResultDAO assessmentResultDAO;

    @GetMapping("/assessment")
    public String showAssessmentForm() {
        return "student/assessment_form";
    }
    @GetMapping("/assessment/submit")
    public String redirectToAssessment() {
        return "redirect:/assessment";
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

        // Analyze and score the assessment answers
        String rawAnswers = "Base Score: " + baseScore + " | Extra Score: " + extraScore;
        AssessmentResult result = assessmentService.analyzeAndScore(rawAnswers, totalScore, feedbackText);

        // Set the studentId (example: retrieve from session or request)
        Long studentId = 1L; // Replace with actual logic to fetch studentId
        result.setStudentId(studentId);

        // Save the result to the database
        assessmentResultDAO.save(result);

        // Add the result to the model for the results page
        model.addAttribute("result", result);

        return "student/assessment_result";
    }

    @GetMapping("/assessment/download")
    public ResponseEntity<String> downloadReport(@RequestParam(name = "data", defaultValue = "") String data) {
        StringBuilder fileContent = new StringBuilder();
        
        // Header
        fileContent.append("╔════════════════════════════════════════════════════════════════╗\n");
        fileContent.append("║          MINDFULBYTES ASSESSMENT HISTORY REPORT                 ║\n");
        fileContent.append("║              Mental Health & Wellness Tracking                  ║\n");
        fileContent.append("╚════════════════════════════════════════════════════════════════╝\n\n");
        
        // Report Generation Date
        fileContent.append("Report Generated: ").append(java.time.LocalDateTime.now())
                .append("\nStudent ID: 1 (Default)\n");
        fileContent.append("═".repeat(70)).append("\n\n");
        
        // Current Assessment Data (if provided)
        if (!data.isEmpty()) {
            fileContent.append("📊 CURRENT ASSESSMENT\n");
            fileContent.append("─".repeat(70)).append("\n");
            fileContent.append(data).append("\n\n");
        }
        
        // Assessment History
        java.util.List<AssessmentResult> allAssessments = assessmentResultDAO.findAll();
        
        if (allAssessments.isEmpty()) {
            fileContent.append("📋 ASSESSMENT HISTORY\n");
            fileContent.append("─".repeat(70)).append("\n");
            fileContent.append("No assessment records found.\n\n");
        } else {
            fileContent.append("📋 COMPLETE ASSESSMENT HISTORY\n");
            fileContent.append("─".repeat(70)).append("\n");
            fileContent.append("Total Assessments: ").append(allAssessments.size()).append("\n\n");
            
            // Summary Statistics
            double averageScore = allAssessments.stream()
                    .mapToDouble(AssessmentResult::getScore)
                    .average()
                    .orElse(0.0);
            double maxScore = allAssessments.stream()
                    .mapToDouble(AssessmentResult::getScore)
                    .max()
                    .orElse(0.0);
            double minScore = allAssessments.stream()
                    .mapToDouble(AssessmentResult::getScore)
                    .min()
                    .orElse(0.0);
            
            fileContent.append("📈 SUMMARY STATISTICS\n");
            fileContent.append("  Average Score: ").append(String.format("%.2f", averageScore)).append("\n");
            fileContent.append("  Highest Score: ").append(String.format("%.2f", maxScore)).append("\n");
            fileContent.append("  Lowest Score: ").append(String.format("%.2f", minScore)).append("\n");
            fileContent.append("  Total Assessments: ").append(allAssessments.size()).append("\n\n");
            
            // Detailed Assessment List
            fileContent.append("📝 DETAILED ASSESSMENT RECORDS\n");
            fileContent.append("─".repeat(70)).append("\n\n");
            
            int index = 1;
            for (AssessmentResult assessment : allAssessments) {
                fileContent.append("Assessment #").append(index).append("\n");
                fileContent.append("  ID: ").append(assessment.getId()).append("\n");
                fileContent.append("  Date: ").append(assessment.getCreatedAt()).append("\n");
                fileContent.append("  Score: ").append(String.format("%.2f", assessment.getScore())).append("\n");
                fileContent.append("  Raw Answers: ").append(assessment.getRawAnswers()).append("\n");
                fileContent.append("  Feedback: ").append(assessment.getFeedback()).append("\n");
                fileContent.append("─".repeat(70)).append("\n\n");
                index++;
            }
        }
        
        // Footer with Recommendations
        fileContent.append("💡 RECOMMENDATIONS\n");
        fileContent.append("─".repeat(70)).append("\n");
        fileContent.append("• Regular assessments help track your mental health progress\n");
        fileContent.append("• Share this report with your counselor for personalized support\n");
        fileContent.append("• Consider booking a session if stress levels are elevated\n");
        fileContent.append("• Access our library for wellness resources and coping strategies\n\n");
        
        // Closing
        fileContent.append("═".repeat(70)).append("\n");
        fileContent.append("For support, please contact your assigned counselor or visit:\n");
        fileContent.append("📧 support@mindfulbytes.com\n");
        fileContent.append("🌐 www.mindfulbytes.com\n\n");
        fileContent.append("Report confidential - for personal use only\n");
        
        // Return as downloadable file
        java.time.LocalDate today = java.time.LocalDate.now();
        String filename = "MindfulBytes_Assessment_Report_" + today + ".txt";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileContent.toString());
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        model.addAttribute("history", assessmentResultDAO.findAll());
        return "student/history";
    }

    // ===== UPDATE OPERATIONS =====
    @GetMapping("/assessment/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var result = assessmentResultDAO.findById(id);
        if (result.isPresent()) {
            model.addAttribute("result", result.get());
            return "student/assessment_edit";
        }
        return "redirect:/history";
    }

    @PostMapping("/assessment/update/{id}")
    public String updateAssessment(
            @PathVariable Long id,
            @RequestParam String feedback,
            @RequestParam(defaultValue = "0") double score) {
        
        var result = assessmentResultDAO.findById(id);
        if (result.isPresent()) {
            AssessmentResult assessment = result.get();
            assessment.setFeedback(feedback);
            assessment.setScore(score);
            assessmentResultDAO.save(assessment);
        }
        return "redirect:/history";
    }

    // ===== DELETE OPERATIONS =====
    @GetMapping("/assessment/delete/{id}")
    public String deleteAssessment(@PathVariable Long id) {
        if (assessmentResultDAO.existsById(id)) {
            assessmentResultDAO.deleteById(id);
        }
        return "redirect:/history";
    }
}
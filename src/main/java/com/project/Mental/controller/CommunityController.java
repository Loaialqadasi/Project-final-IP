package com.project.Mental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/student")
public class CommunityController {

    @GetMapping("/library")
    public String showLibraryPage() {
        return "student/library";
    }

    @GetMapping("/chatbot")
    public String showChatbotPage() {
        return "student/chatbot";
    }

    @GetMapping("/counselors")
    public String showCounselorsPage() {
        return "student/counselors";
    }

    @GetMapping("/booking/{counselorId}")
    public String showBookingPage(@PathVariable("counselorId") Long counselorId, Model model) {

        String counselorName = "";
        if (counselorId == 1) {
            counselorName = "Dr. Nurul Aida";
        } else if (counselorId == 2) {
            counselorName = "Dr. Alex Park";
        } else if (counselorId == 3) {
            counselorName = "Dr. Leila Thompson";
        } else if (counselorId == 4) {
            counselorName = "Dr. Maria Flores";
        } else {
            counselorName = "Unknown Counselor";
        }

        model.addAttribute("counselorName", counselorName);

        return "student/booking";
    }

    @GetMapping("/booking/success")
    public String showBookingSuccessPage() {
    return "student/booking-success";
    }

        /**
     * Handles the form submission from the booking page.
     * Mapped to: POST /student/booking/submit
     * @return A redirect to the booking success page.
     */
    @PostMapping("/booking/submit")
    public String handleBookingSubmission() {
    // In a real application, you would get the selected day and time here
    // and save the appointment to the database.

    // For now, we just redirect to the success page.
        return "redirect:/student/booking/success";
    }

    record Resource(String type, String title, String description, String duration) {}

    @GetMapping("/toolkit/{toolkitId}")
    public String showToolkitPage(@PathVariable("toolkitId") Long toolkitId, Model model) {
        String toolkitTitle = "";
        List<Resource> resources = new ArrayList<>();

        // --- Temporary logic to simulate getting data from a database ---
        if (toolkitId == 1) {
            toolkitTitle = "Mindful Moments";
            resources.add(new Resource("Video", "Guided Meditation for Inner Strength", "A 10-minute video to find your center.", "10 min video"));
            resources.add(new Resource("Audio", "3-Minute Mindful Reset Exercise", "A quick audio exercise to calm your mind.", "3 min audio"));
            resources.add(new Resource("Article", "The Art of Being Present", "Learn the fundamentals of mindfulness.", "5 min read"));
        } else if (toolkitId == 2) {
            toolkitTitle = "Resilience Toolkit";
            resources.add(new Resource("Article", "The Science of Bouncing Back", "Start here to learn the fundamentals.", "5 min read"));
            resources.add(new Resource("Audio", "Positive Affirmations for Tough Days", "Listen to these powerful affirmations.", "3 min audio"));
            resources.add(new Resource("Video", "Building Your Mental Strength", "A practical guide to becoming more resilient.", "12 min video"));
        }
        // --- End of temporary logic ---

        // Add the title and the list of resources to the model
        model.addAttribute("toolkitTitle", toolkitTitle);
        model.addAttribute("resources", resources);

        return "student/toolkit"; // We will create this HTML file next
    }
}
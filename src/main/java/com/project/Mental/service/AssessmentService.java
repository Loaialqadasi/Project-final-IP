package com.project.Mental.service;

import org.springframework.stereotype.Service;

import com.project.Mental.model.AssessmentResult;

@Service
public class AssessmentService {

    public AssessmentResult analyzeAndScore(String rawAnswers, int score, String feedback) {
        // Create and return the AssessmentResult object
        AssessmentResult result = new AssessmentResult();
        result.setRawAnswers(rawAnswers);
        result.setScore((double) score);
        result.setFeedback(feedback);
        return result;
    }
}
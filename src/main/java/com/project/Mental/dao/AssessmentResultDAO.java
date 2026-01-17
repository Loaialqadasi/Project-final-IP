package com.project.Mental.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Mental.model.AssessmentResult;

@Repository
public interface AssessmentResultDAO extends JpaRepository<AssessmentResult, Long> {
    // Additional query methods can be defined here if needed
}
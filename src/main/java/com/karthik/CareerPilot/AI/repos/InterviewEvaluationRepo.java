package com.karthik.CareerPilot.AI.repos;

import com.karthik.CareerPilot.AI.entities.InterviewEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewEvaluationRepo
        extends JpaRepository<InterviewEvaluation, Long> {

    Optional<InterviewEvaluation>
    findByQuestionId(Long questionId);
}
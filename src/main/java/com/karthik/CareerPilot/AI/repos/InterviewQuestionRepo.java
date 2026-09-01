package com.karthik.CareerPilot.AI.repos;

import com.karthik.CareerPilot.AI.entities.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewQuestionRepo
        extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findBySessionId(Long sessionId);
}
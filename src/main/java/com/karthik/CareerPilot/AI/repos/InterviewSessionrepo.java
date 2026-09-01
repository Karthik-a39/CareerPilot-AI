package com.karthik.CareerPilot.AI.repos;

import com.karthik.CareerPilot.AI.entities.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewSessionrepo extends JpaRepository<InterviewSession,Long> {


    List<InterviewSession> findByUserId(Long userId);
}

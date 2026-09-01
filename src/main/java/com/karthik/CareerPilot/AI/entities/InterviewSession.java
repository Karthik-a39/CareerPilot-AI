package com.karthik.CareerPilot.AI.entities;

import com.karthik.CareerPilot.AI.enums.Difficulty;
import com.karthik.CareerPilot.AI.enums.InterviewStatus;
import com.karthik.CareerPilot.AI.enums.InterviewType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="interview_sessions")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String targetRole;

    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private int totalQuestions;

    private int currentQuestion;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    private double finalScore;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

}

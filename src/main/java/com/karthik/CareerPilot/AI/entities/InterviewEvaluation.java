package com.karthik.CareerPilot.AI.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interview_evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    private int technicalAccuracy;

    private int conceptualUnderstanding;

    private int completeness;

    private int practicalKnowledge;

    private int communication;

    private double finalScore;

    @Column(columnDefinition = "TEXT")
    private String strengths;

    @Column(columnDefinition = "TEXT")
    private String weaknesses;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(columnDefinition = "TEXT")
    private String idealAnswer;
}
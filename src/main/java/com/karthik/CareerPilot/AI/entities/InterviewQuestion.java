package com.karthik.CareerPilot.AI.entities;

import com.karthik.CareerPilot.AI.enums.Difficulty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "interview_questions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewQuestion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sessionId;
    private int questionNumber;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(length = 255)
    private String category;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(columnDefinition = "TEXT")
    private String userAnswer;

    private Double score;

    @Column(columnDefinition = "TEXT")
    private String feedback;
}

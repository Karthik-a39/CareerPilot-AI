package com.karthik.CareerPilot.AI.records;

public record StartInterviewRequest(
        String targetRole,
        String interviewType,
        String difficulty,
        int totalQuestions
) {
}
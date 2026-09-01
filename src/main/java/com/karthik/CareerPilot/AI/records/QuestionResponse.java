package com.karthik.CareerPilot.AI.records;

public record QuestionResponse(
        Long questionId,
        int questionNumber,
        String question,
        String category,
        String difficulty
) {
}
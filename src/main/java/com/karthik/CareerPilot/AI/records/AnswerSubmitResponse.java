package com.karthik.CareerPilot.AI.records;

public record AnswerSubmitResponse(
        EvaluationResponse evaluation,
        double score,
        int currentQuestion,
        int totalQuestions,
        boolean interviewComplete,
        QuestionResponse nextQuestion
) {
}

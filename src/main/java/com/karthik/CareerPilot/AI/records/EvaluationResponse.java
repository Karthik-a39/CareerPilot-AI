package com.karthik.CareerPilot.AI.records;

public record EvaluationResponse(

        Integer technicalAccuracy,

        Integer conceptualUnderstanding,

        Integer completeness,

        Integer practicalKnowledge,

        Integer communication,

        String strengths,

        String weaknesses,

        String feedback,

        String idealAnswer
) {
}
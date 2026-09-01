package com.karthik.CareerPilot.AI.records;

import java.util.List;

public record InterviewReportResponse(

        String targetRole,

        double overallScore,

        int totalQuestions,

        String strengths,

        String weaknesses,

        List<String> recommendedTopics,

        String recommendation
) {
}
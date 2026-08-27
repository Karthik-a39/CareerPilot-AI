package com.karthik.CareerPilot.AI.records;

import java.util.List;

public record AIRoadmap(
        String careerGoal,
        String roadmapSummary,
        List<MonthPlan> months
) {}

 record MonthPlan(
        int month,
        String title,
        List<String> topics,
        List<String> skillsToDevelop,
        List<String> resources,
        List<String> projects,
        List<String> outcomes
) {}
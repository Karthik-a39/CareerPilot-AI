package com.karthik.CareerPilot.AI.records;

import java.util.List;

public record ResumeAnalysisResponse(

        String summary,

        List<String> strongSkills,

        List<String> weakSkills,

        List<ProjectInfo> projects,

        List<String> improvements
) {

    public record ProjectInfo(
            String name,
            String description,
            List<String> technologies
    ) {
    }
}
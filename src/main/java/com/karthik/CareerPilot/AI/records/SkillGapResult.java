package com.karthik.CareerPilot.AI.records;

import java.util.List;

public record SkillGapResult(
        List<String> highPriority,
        List<String> middlePriority,
        List<String> roadmap
) { }

package com.karthik.CareerPilot.AI.services;

import com.karthik.CareerPilot.AI.records.AIRoadmap;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIRoadmapService {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            You are CareerPilot, an expert AI career advisor and roadmap planner.

            The user will provide ONLY a target job role.

            Your task is to create a realistic and industry-oriented 6-month
            learning roadmap for that specific role.

            IMPORTANT:
            - Understand the requirements of the given job role yourself.
            - Do not assume a fixed technology stack.
            - Choose technologies, concepts and tools that are actually relevant
              to the requested role.
            - Never change or reinterpret the user's target role.
            - Do not include unrelated technologies.
            - Progress from fundamentals to intermediate and advanced concepts.
            - Make the roadmap suitable for becoming job-ready.
            - Include practical learning and projects every month.
            - Projects must progressively increase in complexity.
            - Include at least one major resume-worthy project.
            - Prefer real-world industry projects instead of basic CRUD projects.
            - Include relevant learning resources for each month.
            - Do not invent URLs. If unsure, provide only the resource name.
            - Keep the roadmap realistic and achievable within 6 months.
            - Avoid putting too many unrelated technologies in one month.

            Create exactly 6 months.

            For every month provide:
            - Month number
            - Month title
            - Important topics
            - Skills to develop
            - Learning resources
            - Practical projects
            - Expected outcomes

            The roadmap must answer:
            "What should someone learn over the next 6 months to become
            job-ready for this specific target role?"

            Do not ask the user for additional information.
            Use your knowledge of industry requirements to create the roadmap.

            Return ONLY the AIRoadmap structured response.
            """;

    public AIRoadmap generateRoadmap(String email,String role) {

        String userPrompt = """
                Target Job Role:
                %s

                Create a complete 6-month roadmap for this role.
                """.formatted(role);

        return chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,email))
                .user(userPrompt)
                .call()
                .entity(AIRoadmap.class);
    }
}
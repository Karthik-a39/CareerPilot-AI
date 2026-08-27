package com.karthik.CareerPilot.AI.services;

import com.karthik.CareerPilot.AI.records.ResumeAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeAIService {

    private final ChatClient chatClient;

    public ResumeAnalysisResponse analyzeResume(String conversationID,String resumeText) {

        String prompt = """
                Analyze the following resume.
                make a quick Respose with in 5sec

                Your job is to identify:

                1. Overall professional summary
                2. Strong technical skills
                3. Weak or missing skills
                4. Projects mentioned in the resume
                5. Technologies used in each project
                6. Specific improvements the candidate should make

                Important rules:

                - Only use information present in the resume.
                - Do not invent experience, skills, projects, or technologies.
                - If something is not present, return an empty list.
                - Keep the summary concise.
                - Give practical improvement suggestions.

                Resume:

                %s
                """.formatted(resumeText);

        return chatClient
                .prompt()
                .user(prompt)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,conversationID))
                .call()
                .entity(ResumeAnalysisResponse.class);
    }
}
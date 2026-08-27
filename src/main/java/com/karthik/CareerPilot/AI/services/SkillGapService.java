package com.karthik.CareerPilot.AI.services;

import com.karthik.CareerPilot.AI.records.SkillGapResult;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillGapService {

    private final ChatClient chatClient;

    public SkillGapResult getResult(String conversationID,String resumeText){

        String prompt = """
        Analyze the candidate's resume and identify their skill gaps.

        The candidate is preparing for software engineering roles.

        Analyze the skills, technologies, projects, and experience
        present in the resume.

        Identify the skills that the candidate should improve or learn.

        Return EXACTLY ONE JSON OBJECT with these three fields:

        {
          "highPriority": [],
          "middlePriority": [],
          "roadmap": []
        }

        Rules:

        highPriority:
        - Skills that are most important for the candidate to improve.
        - Focus on skills that are missing or weak in the resume.
        - Do not include skills that the resume clearly demonstrates strongly.

        middlePriority:
        - Useful skills that should be improved after high-priority skills.
        - These are important but less urgent.

        roadmap:
        - Provide a logical learning order for the identified skill gaps.
        - Start with foundational concepts.
        - Progress toward intermediate and advanced concepts.
        - Each item must be a specific learning topic or milestone.

        Important:
        - Use only information available in the resume.
        - Do not invent experience or skills.
        - Focus on software engineering skills.
        - Do not provide explanations outside the JSON object.
        - Each field must contain a JSON array of strings.
        - Return only the JSON object.

        Resume:

        %s
        """.formatted(resumeText);

        return chatClient.prompt()
                .user(prompt)
                .advisors(a-> a.param(ChatMemory.CONVERSATION_ID,conversationID))
                .call()
                .entity(SkillGapResult.class);


    }
}

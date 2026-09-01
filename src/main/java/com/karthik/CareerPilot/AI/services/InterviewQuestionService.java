package com.karthik.CareerPilot.AI.services;

import com.karthik.CareerPilot.AI.records.GeneratedQuestion;
import com.karthik.CareerPilot.AI.records.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final ChatClient chatClient;

    public QuestionResponse generateQuestion(
            String conversationId,
            String role,
            String type,
            String difficulty,
            String skillGaps,
            List<String> previousQuestions
    ) {
        String previous = previousQuestions == null || previousQuestions.isEmpty()
                ? "None"
                : String.join("\n- ", previousQuestions);

        String prompt = """
                Generate one interview question.

                Target Role:
                %s

                Interview Type:
                %s

                Difficulty:
                %s

                Candidate Skill Gaps:
                %s

                Previous Questions:
                %s

                Rules:
                - Ask only one question.
                - Do not repeat previous questions.
                - Question must be relevant to the target role.
                - Test real understanding.
                - Prefer practical interview questions.
                - category should be a short topic label such as Java, SQL, or System Design.
                """.formatted(
                role,
                type,
                difficulty,
                skillGaps == null || skillGaps.isBlank() ? "Not provided" : skillGaps,
                previous
        );

        GeneratedQuestion generated = chatClient.prompt()
                .system("""
                        You are CareerPilot AI,
                        a professional technical interviewer.

                        Conduct realistic interviews.
                        Ask questions appropriate to the
                        candidate's target role and experience.
                        Return structured JSON only.
                        """)
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(GeneratedQuestion.class);

        if (generated == null || generated.question() == null || generated.question().isBlank()) {
            throw new RuntimeException("Failed to generate an interview question. Try again.");
        }

        return new QuestionResponse(
                null,
                0,
                generated.question(),
                generated.category() == null ? type : generated.category(),
                difficulty
        );
    }
}

package com.karthik.CareerPilot.AI.services;

import com.karthik.CareerPilot.AI.entities.InterviewQuestion;
import com.karthik.CareerPilot.AI.entities.InterviewSession;
import com.karthik.CareerPilot.AI.records.EvaluationResponse;
import com.karthik.CareerPilot.AI.records.InterviewReportResponse;
import com.karthik.CareerPilot.AI.repos.InterviewQuestionRepo;
import com.karthik.CareerPilot.AI.repos.InterviewSessionrepo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AnswerEvaluationService {

    private final ChatClient chatClient;
    private final InterviewSessionrepo interviewSessionrepo;
    private final InterviewQuestionRepo questionRepo;

    public EvaluationResponse evaluate(
            String conversationId,
            String role,
            String question,
            String answer
    ) {

        String prompt = """
                Evaluate the candidate's interview answer.

                Target Role:
                %s

                Question:
                %s

                Candidate Answer:
                %s

                Evaluate:

                1. Technical accuracy
                2. Conceptual understanding
                3. Completeness
                4. Practical knowledge
                5. Communication

                Give constructive feedback.
                Provide an ideal answer.
                Score each category from 0 to 10 as integers.
                """.formatted(
                role,
                question,
                answer
        );

        EvaluationResponse evaluation = chatClient.prompt()
                .system("""
                        You are an expert technical interviewer.

                        Evaluate answers fairly.
                        Do not give high scores merely because
                        the answer sounds confident.

                        Score each category from 0 to 10.
                        Identify missing concepts.
                        Provide useful interview feedback.
                        Return structured JSON only.
                        """)
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .entity(EvaluationResponse.class);

        if (evaluation == null) {
            throw new RuntimeException("Failed to evaluate the answer. Try again.");
        }

        return evaluation;
    }

    public InterviewReportResponse generateReport(Long sessionId) {

        InterviewSession session =
                interviewSessionrepo.findById(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException("Interview session not found"));

        List<InterviewQuestion> questions =
                questionRepo.findBySessionId(sessionId);

        double overallScore = calculateOverallScore(questions);

        String interviewData = buildInterviewData(
                session,
                questions,
                overallScore
        );

        InterviewReportResponse report = chatClient.prompt()
                .system("""
                        You are an expert technical interviewer.

                        Analyze the candidate's complete mock interview.

                        Evaluate:
                        - Technical knowledge
                        - Problem solving
                        - Understanding of concepts
                        - Quality of answers
                        - Weak areas
                        - Topics that should be studied next

                        Give an honest and practical assessment.

                        Do not invent skills or information that is not present
                        in the interview data.
                        Return structured JSON only.
                        """)
                .user(interviewData)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "interview-report-" + sessionId))
                .call()
                .entity(InterviewReportResponse.class);

        if (report == null) {
            throw new RuntimeException("Failed to generate the interview report.");
        }

        return new InterviewReportResponse(
                session.getTargetRole(),
                overallScore,
                questions.size(),
                report.strengths(),
                report.weaknesses(),
                report.recommendedTopics(),
                report.recommendation()
        );
    }

    private String buildInterviewData(
            InterviewSession session,
            List<InterviewQuestion> questions,
            double overallScore
    ) {

        StringBuilder data = new StringBuilder();

        data.append("""
                Target Role: %s
                Interview Type: %s
                Difficulty: %s
                Total Questions: %d
                Overall Score: %.2f

                QUESTIONS AND ANSWERS:

                """.formatted(
                session.getTargetRole(),
                session.getInterviewType(),
                session.getDifficulty(),
                questions.size(),
                overallScore
        ));

        for (InterviewQuestion question : questions) {

            data.append("""

                    Question %d:
                    %s

                    Candidate Answer:
                    %s

                    Score:
                    %.2f

                    Feedback:
                    %s

                    """.formatted(
                    question.getQuestionNumber(),
                    question.getQuestion(),
                    question.getUserAnswer() == null ? "(no answer)" : question.getUserAnswer(),
                    question.getScore() == null ? 0.0 : question.getScore(),
                    question.getFeedback() == null ? "None" : question.getFeedback()
            ));
        }

        return data.toString();
    }

    private double calculateOverallScore(
            List<InterviewQuestion> questions
    ) {

        List<Double> scores = questions.stream()
                .map(InterviewQuestion::getScore)
                .filter(Objects::nonNull)
                .toList();

        if (scores.isEmpty()) {
            return 0;
        }

        return scores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }
}

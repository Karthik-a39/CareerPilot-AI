package com.karthik.CareerPilot.AI.services;

import com.karthik.CareerPilot.AI.entities.InterviewEvaluation;
import com.karthik.CareerPilot.AI.entities.InterviewQuestion;
import com.karthik.CareerPilot.AI.entities.InterviewSession;
import com.karthik.CareerPilot.AI.enums.Difficulty;
import com.karthik.CareerPilot.AI.enums.InterviewStatus;
import com.karthik.CareerPilot.AI.enums.InterviewType;
import com.karthik.CareerPilot.AI.records.AnswerSubmitResponse;
import com.karthik.CareerPilot.AI.records.EvaluationResponse;
import com.karthik.CareerPilot.AI.records.InterviewReportResponse;
import com.karthik.CareerPilot.AI.records.QuestionResponse;
import com.karthik.CareerPilot.AI.records.StartInterviewRequest;
import com.karthik.CareerPilot.AI.repos.InterviewEvaluationRepo;
import com.karthik.CareerPilot.AI.repos.InterviewQuestionRepo;
import com.karthik.CareerPilot.AI.repos.InterviewSessionrepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MockInterviewService {

    private final InterviewSessionrepo sessionRepo;
    private final InterviewQuestionRepo questionRepo;
    private final InterviewEvaluationRepo evaluationRepo;

    private final InterviewQuestionService questionService;
    private final AnswerEvaluationService evaluationService;

    @Transactional
    public InterviewSession startInterview(
            StartInterviewRequest request,
            Long userId
    ) {

        int totalQuestions = request.totalQuestions() <= 0
                ? 5
                : Math.min(request.totalQuestions(), 12);

        InterviewSession session =
                InterviewSession.builder()
                        .userId(userId)
                        .targetRole(request.targetRole())
                        .interviewType(parseEnum(InterviewType.class, request.interviewType(), "interview type"))
                        .difficulty(parseEnum(Difficulty.class, request.difficulty(), "difficulty"))
                        .totalQuestions(totalQuestions)
                        .currentQuestion(0)
                        .status(InterviewStatus.IN_PROGRESS)
                        .startedAt(LocalDateTime.now())
                        .build();

        return sessionRepo.save(session);
    }

    public List<InterviewSession> listSessions(Long userId) {
        return sessionRepo.findByUserId(userId).stream()
                .sorted(Comparator.comparing(InterviewSession::getStartedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    public InterviewSession getSession(Long sessionId, Long userId) {
        return ownedSession(sessionId, userId);
    }

    public QuestionResponse generateFirstQuestion(
            Long sessionId,
            Long userId,
            String email
    ) {

        InterviewSession session = ownedSession(sessionId, userId);
        ensureInProgress(session);

        List<InterviewQuestion> existing = questionRepo.findBySessionId(sessionId);
        if (!existing.isEmpty()) {
            InterviewQuestion latest = existing.stream()
                    .max(Comparator.comparingInt(InterviewQuestion::getQuestionNumber))
                    .orElseThrow();
            return toResponse(latest);
        }

        return persistGeneratedQuestion(session, email, List.of(), 1);
    }

    public AnswerSubmitResponse submitAnswer(
            Long sessionId,
            Long questionId,
            String answer,
            Long userId
    ) {

        if (answer == null || answer.isBlank()) {
            throw new RuntimeException("Answer cannot be empty");
        }

        InterviewQuestion question =
                questionRepo.findById(questionId)
                        .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getSessionId().equals(sessionId)) {
            throw new RuntimeException("Question does not belong to this session");
        }

        InterviewSession session = ownedSession(question.getSessionId(), userId);
        ensureInProgress(session);

        if (question.getUserAnswer() != null) {
            throw new RuntimeException("This question has already been answered");
        }

        EvaluationResponse evaluation =
                evaluationService.evaluate(
                        "interview-eval-" + sessionId + "-" + questionId,
                        session.getTargetRole(),
                        question.getQuestion(),
                        answer
                );

        double score = calculateScore(evaluation);

        question.setUserAnswer(answer);
        question.setScore(score);
        question.setFeedback(evaluation.feedback());
        questionRepo.save(question);

        evaluationRepo.save(
                InterviewEvaluation.builder()
                        .questionId(question.getId())
                        .technicalAccuracy(scoreValue(evaluation.technicalAccuracy()))
                        .conceptualUnderstanding(scoreValue(evaluation.conceptualUnderstanding()))
                        .completeness(scoreValue(evaluation.completeness()))
                        .practicalKnowledge(scoreValue(evaluation.practicalKnowledge()))
                        .communication(scoreValue(evaluation.communication()))
                        .finalScore(score)
                        .strengths(evaluation.strengths())
                        .weaknesses(evaluation.weaknesses())
                        .feedback(evaluation.feedback())
                        .idealAnswer(evaluation.idealAnswer())
                        .build()
        );

        session.setDifficulty(determineNextDifficulty(score));
        sessionRepo.save(session);

        long answered = questionRepo.findBySessionId(sessionId).stream()
                .filter(q -> q.getUserAnswer() != null)
                .count();
        boolean complete = answered >= session.getTotalQuestions();

        return new AnswerSubmitResponse(
                evaluation,
                round(score),
                session.getCurrentQuestion(),
                session.getTotalQuestions(),
                complete,
                null
        );
    }

    public QuestionResponse nextQuestion(Long sessionId, Long userId, String email) {
        InterviewSession session = ownedSession(sessionId, userId);
        ensureInProgress(session);

        List<InterviewQuestion> questions = questionRepo.findBySessionId(sessionId);
        if (questions.isEmpty()) {
            return persistGeneratedQuestion(session, email, List.of(), 1);
        }

        InterviewQuestion latest = questions.stream()
                .max(Comparator.comparingInt(InterviewQuestion::getQuestionNumber))
                .orElseThrow();

        if (latest.getUserAnswer() == null) {
            return toResponse(latest);
        }

        long answered = questions.stream()
                .filter(q -> q.getUserAnswer() != null)
                .count();
        if (answered >= session.getTotalQuestions()) {
            throw new RuntimeException("Interview is already complete");
        }

        List<String> previousQuestions = questions.stream()
                .map(InterviewQuestion::getQuestion)
                .toList();

        return persistGeneratedQuestion(
                session,
                email,
                previousQuestions,
                latest.getQuestionNumber() + 1
        );
    }

    public InterviewReportResponse completeInterview(Long sessionId, Long userId) {
        InterviewSession session = ownedSession(sessionId, userId);

        List<InterviewQuestion> questions = questionRepo.findBySessionId(sessionId);
        long answered = questions.stream().filter(q -> q.getUserAnswer() != null).count();
        if (answered == 0) {
            throw new RuntimeException("Answer at least one question before completing the interview");
        }

        double finalScore = questions.stream()
                .filter(q -> q.getScore() != null)
                .mapToDouble(InterviewQuestion::getScore)
                .average()
                .orElse(0);

        session.setFinalScore(round(finalScore));
        session.setStatus(InterviewStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());
        sessionRepo.save(session);

        return evaluationService.generateReport(sessionId);
    }

    public InterviewReportResponse getReport(Long sessionId, Long userId) {
        ownedSession(sessionId, userId);
        return evaluationService.generateReport(sessionId);
    }

    public double calculateScore(EvaluationResponse evaluation) {
        return scoreValue(evaluation.technicalAccuracy()) * 0.40
                + scoreValue(evaluation.conceptualUnderstanding()) * 0.25
                + scoreValue(evaluation.completeness()) * 0.15
                + scoreValue(evaluation.practicalKnowledge()) * 0.10
                + scoreValue(evaluation.communication()) * 0.10;
    }

    private QuestionResponse persistGeneratedQuestion(
            InterviewSession session,
            String email,
            List<String> previousQuestions,
            int questionNumber
    ) {
        QuestionResponse response =
                questionService.generateQuestion(
                        "interview-q-" + session.getId(),
                        session.getTargetRole(),
                        session.getInterviewType().name(),
                        session.getDifficulty().name(),
                        "",
                        previousQuestions
                );

        InterviewQuestion question =
                InterviewQuestion.builder()
                        .sessionId(session.getId())
                        .questionNumber(questionNumber)
                        .question(response.question())
                        .category(truncate(response.category(), 240))
                        .difficulty(session.getDifficulty())
                        .build();

        questionRepo.save(question);
        session.setCurrentQuestion(questionNumber);
        sessionRepo.save(session);

        return toResponse(question);
    }

    private Difficulty determineNextDifficulty(double score) {
        if (score >= 8) {
            return Difficulty.HARD;
        }
        if (score >= 5) {
            return Difficulty.MEDIUM;
        }
        return Difficulty.EASY;
    }

    private InterviewSession ownedSession(Long sessionId, Long userId) {
        InterviewSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Interview session not found"));
        if (!session.getUserId().equals(userId)) {
            throw new RuntimeException("You do not have access to this interview");
        }
        return session;
    }

    private void ensureInProgress(InterviewSession session) {
        if (session.getStatus() != InterviewStatus.IN_PROGRESS) {
            throw new RuntimeException("This interview is no longer in progress");
        }
    }

    private QuestionResponse toResponse(InterviewQuestion question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestionNumber(),
                question.getQuestion(),
                question.getCategory(),
                question.getDifficulty() == null ? null : question.getDifficulty().name()
        );
    }

    private <E extends Enum<E>> E parseEnum(Class<E> type, String value, String label) {
        if (value == null || value.isBlank()) {
            throw new RuntimeException("Missing " + label);
        }
        try {
            return Enum.valueOf(type, value.trim().toUpperCase().replace(' ', '_').replace('-', '_'));
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Unsupported " + label + ": " + value);
        }
    }

    private double round(double score) {
        return Math.round(score * 100.0) / 100.0;
    }

    private int scoreValue(Integer value) {
        if (value == null) {
            return 0;
        }
        return Math.max(0, Math.min(10, value));
    }

    private String truncate(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() <= max ? value : value.substring(0, max);
    }
}

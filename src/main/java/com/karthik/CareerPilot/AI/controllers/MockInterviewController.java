package com.karthik.CareerPilot.AI.controllers;

import com.karthik.CareerPilot.AI.entities.InterviewSession;
import com.karthik.CareerPilot.AI.entities.UserEntity;
import com.karthik.CareerPilot.AI.records.AnswerSubmitResponse;
import com.karthik.CareerPilot.AI.records.InterviewReportResponse;
import com.karthik.CareerPilot.AI.records.QuestionResponse;
import com.karthik.CareerPilot.AI.records.StartInterviewRequest;
import com.karthik.CareerPilot.AI.records.SubmitAnswerRequest;
import com.karthik.CareerPilot.AI.repos.UserRepo;
import com.karthik.CareerPilot.AI.services.MockInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class MockInterviewController {

    private final MockInterviewService interviewService;
    private final UserRepo repo;

    @PostMapping
    public InterviewSession startInterview(@RequestBody StartInterviewRequest request) {
        UserEntity user = currentUser();
        return interviewService.startInterview(request, user.getUserId());
    }

    @GetMapping
    public List<InterviewSession> listInterviews() {
        return interviewService.listSessions(currentUser().getUserId());
    }

    @GetMapping("/{sessionId}")
    public InterviewSession getInterview(@PathVariable Long sessionId) {
        return interviewService.getSession(sessionId, currentUser().getUserId());
    }

    @PostMapping("/{sessionId}/start")
    public QuestionResponse firstQuestion(@PathVariable Long sessionId) {
        UserEntity user = currentUser();
        return interviewService.generateFirstQuestion(
                sessionId,
                user.getUserId(),
                user.getEmail()
        );
    }

    @PostMapping("/{sessionId}/questions/{questionId}/answer")
    public AnswerSubmitResponse submitAnswer(
            @PathVariable Long sessionId,
            @PathVariable Long questionId,
            @RequestBody SubmitAnswerRequest request
    ) {
        return interviewService.submitAnswer(
                sessionId,
                questionId,
                request.answer(),
                currentUser().getUserId()
        );
    }

    @PostMapping("/{sessionId}/next")
    public QuestionResponse nextQuestion(@PathVariable Long sessionId) {
        UserEntity user = currentUser();
        return interviewService.nextQuestion(
                sessionId,
                user.getUserId(),
                user.getEmail()
        );
    }

    @PostMapping("/{sessionId}/complete")
    public InterviewReportResponse completeInterview(@PathVariable Long sessionId) {
        return interviewService.completeInterview(sessionId, currentUser().getUserId());
    }

    @GetMapping("/{sessionId}/report")
    public InterviewReportResponse getReport(@PathVariable Long sessionId) {
        return interviewService.getReport(sessionId, currentUser().getUserId());
    }

    private UserEntity currentUser() {
        Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return repo.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
    }
}

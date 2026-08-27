package com.karthik.CareerPilot.AI.controllers;

import com.karthik.CareerPilot.AI.records.ResumeAnalysisResponse;
import com.karthik.CareerPilot.AI.services.PdfTextExtractorService;
import com.karthik.CareerPilot.AI.services.ResumeAIService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final PdfTextExtractorService pdfTextExtractorService;
    private final ResumeAIService resumeAIService;

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResumeAnalysisResponse analyzeResume( Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        String resumeText =
                pdfTextExtractorService
                        .extractText(file);

        String email=authentication.getName();

        return resumeAIService
                .analyzeResume(email,resumeText);
    }
}
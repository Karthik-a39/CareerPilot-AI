package com.karthik.CareerPilot.AI.controllers;

import com.karthik.CareerPilot.AI.records.ResumeAnalysisResponse;
import com.karthik.CareerPilot.AI.records.SkillGapResult;
import com.karthik.CareerPilot.AI.services.PdfTextExtractorService;
import com.karthik.CareerPilot.AI.services.SkillGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/skillgap/")
@RequiredArgsConstructor
public class SkillGapController {


    private final PdfTextExtractorService pdfTextExtractorService;
    private final SkillGapService skillGapService;


    @PostMapping(
            value = "/analyze",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public SkillGapResult analyzeResume(Authentication authentication,
                                        @RequestParam("file") MultipartFile file
    ) throws Exception {

        String resumeText =
                pdfTextExtractorService
                        .extractText(file);

        String email=authentication.getName();

        return skillGapService
                .getResult(email,resumeText);
    }
}

package com.karthik.CareerPilot.AI.controllers;

import com.karthik.CareerPilot.AI.services.KnowledgeBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class KnowledgeBaseController {

    private final KnowledgeBaseService  knowledgeBaseService;


    @PostMapping("/ingest")
    public String ingest()  throws IOException {
        knowledgeBaseService.injectKnowledge();
        return "Knowledge base ingestion completed";
    }
}

package com.karthik.CareerPilot.AI.controllers;

import com.karthik.CareerPilot.AI.services.RagService;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagSearchController {


    private final RagService ragSimilaritySearchService;

    @GetMapping("/search")
    public String search(Authentication authentication, @RequestParam String query){
        String email=authentication.getName();
        return ragSimilaritySearchService.ask(email,query);
    }
}

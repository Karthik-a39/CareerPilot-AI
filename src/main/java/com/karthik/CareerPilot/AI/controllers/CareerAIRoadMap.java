package com.karthik.CareerPilot.AI.controllers;

import com.karthik.CareerPilot.AI.records.AIRoadmap;

import com.karthik.CareerPilot.AI.services.AIRoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class CareerAIRoadMap {

    private final AIRoadmapService roadMapService;

    @GetMapping("/roadmap")
    public AIRoadmap getRoadMAp(Authentication authentication, @RequestParam String role){
        String email=authentication.getName();
        return  roadMapService.generateRoadmap(email,role);
    }
}

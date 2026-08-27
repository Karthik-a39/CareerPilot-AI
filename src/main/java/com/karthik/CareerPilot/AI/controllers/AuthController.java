package com.karthik.CareerPilot.AI.controllers;

import com.karthik.CareerPilot.AI.dtos.LoginRequest;
import com.karthik.CareerPilot.AI.dtos.RegisterRequest;
import com.karthik.CareerPilot.AI.dtos.RegisterResponse;
import com.karthik.CareerPilot.AI.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse getRegister(@Valid @RequestBody RegisterRequest req){
        return authService.getRegister(req);
    }


    @PostMapping("/login")
    public Map<String,String> getLogin(@Valid @RequestBody LoginRequest req){
        return authService.getLogin(req);
    }

    @GetMapping
    public String masg(){
        return "Security set successfully";
    }



}

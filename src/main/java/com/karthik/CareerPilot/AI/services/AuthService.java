package com.karthik.CareerPilot.AI.services;

import com.karthik.CareerPilot.AI.dtos.LoginRequest;
import com.karthik.CareerPilot.AI.dtos.RegisterRequest;
import com.karthik.CareerPilot.AI.dtos.RegisterResponse;
import com.karthik.CareerPilot.AI.entities.UserEntity;
import com.karthik.CareerPilot.AI.repos.UserRepo;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    //register
    public RegisterResponse getRegister(RegisterRequest req){
        UserEntity user=toEntity(req);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return toDto(user);
    }

    private RegisterResponse toDto(UserEntity user) {
        return RegisterResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private UserEntity toEntity(RegisterRequest req) {
        return UserEntity.builder()
                .userId(req.getUserId())
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .createdAt(req.getCreatedAt())
                .build();
    }

    // Login
    public Map<String, String> getLogin(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid credentials");
        }

        UserEntity user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not registered"));
        String key=jwtService.generateToken(req.getEmail());
        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", user.getEmail());
        loginDetails.put("msg", "Login successful!");
        loginDetails.put("token", key);

        return loginDetails;
    }


}

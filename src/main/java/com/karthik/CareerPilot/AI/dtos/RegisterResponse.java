package com.karthik.CareerPilot.AI.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private Long userId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}

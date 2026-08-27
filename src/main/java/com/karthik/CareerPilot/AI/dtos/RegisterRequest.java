package com.karthik.CareerPilot.AI.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private  Long userId;

    @NotBlank(message = "userName Required")
    private String name;
    @NotBlank(message = "user Email Required")
    private String email;
    @Size(min = 6,message = "password must have at least 6 characters")
    private String password;

    private LocalDateTime createdAt;
}

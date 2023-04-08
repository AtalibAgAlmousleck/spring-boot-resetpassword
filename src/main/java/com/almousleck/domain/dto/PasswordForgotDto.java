package com.almousleck.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordForgotDto {
    @Email
    @NotEmpty
    private String email;
}

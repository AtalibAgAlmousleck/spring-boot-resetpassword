package com.almousleck.domain.dto;

import com.almousleck.matchField.FieldMatch;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class PasswordResetDto {
    @NotEmpty(message = "Password is required")
    private String password;
    @NotEmpty(message = "Confirm Password is required")
    private String confirmPassword;
    @NotEmpty(message = "Token is required")
    private String token;
}

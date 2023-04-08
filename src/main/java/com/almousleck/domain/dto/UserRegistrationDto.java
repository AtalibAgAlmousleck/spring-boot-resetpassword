package com.almousleck.domain.dto;

import com.almousleck.matchField.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
        @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})
public class UserRegistrationDto {
    @NotEmpty(message = "First name is required.")
    private String firstName;
    @NotEmpty(message = "Last name is required.")
    private String lastName;
    @NotEmpty(message = "Email is required.")
    @Email(message = "Please provide a valid email address")
    private String email;
    @NotEmpty(message = "Email is required.")
    @Email(message = "Please provide a valid email address")
    private String confirmEmail;
    @NotEmpty(message = "Password is required")
    private String password;
    @NotEmpty(message = "Password is required")
    private String confirmPassword;
}

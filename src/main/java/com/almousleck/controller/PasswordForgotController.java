package com.almousleck.controller;

import com.almousleck.domain.PasswordResetToken;
import com.almousleck.domain.User;
import com.almousleck.domain.dto.PasswordForgotDto;
import com.almousleck.email.Mail;
import com.almousleck.repository.PasswordResetTokenRepository;
import com.almousleck.service.EmailService;
import com.almousleck.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/forgot-password")
@RequiredArgsConstructor
public class PasswordForgotController {
    private final UserService userService;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotPasswordDto() {
        return new PasswordForgotDto();
    }
    @GetMapping
    public String displayForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping
    public String processForgotPassword(@ModelAttribute("forgotPasswordForm")
                                        @Valid PasswordForgotDto forgotDto,
                                        BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "forgot-password";
        }
        User user = userService.findByEmail(forgotDto.getEmail());
        if (user == null) {
            result.reject("email", null, "The given email not found.");
            return "forgot-password";
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        tokenRepository.save(token);

        Mail mail = new Mail();
        mail.setFrom("no-reply@tahidjart.tech");
        mail.setTo(user.getEmail());
        mail.setSubject("Password reset request");

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("user", user);
        model.put("signature", "https://tahidjart.tech");
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        model.put("resetUrl", url + "/reset-password?token=" + token.getToken());
        mail.setModel(model);
        emailService.sendEmail(mail);

        return "redirect:/forgot-password?success";
    }
}

package com.almousleck.controller;

import com.almousleck.domain.PasswordResetToken;
import com.almousleck.domain.User;
import com.almousleck.domain.dto.PasswordResetDto;
import com.almousleck.repository.PasswordResetTokenRepository;
import com.almousleck.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reset-password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute("passwordResetForm")
    public PasswordResetDto passwordResetDto() {
        return new PasswordResetDto();
    }

    @GetMapping
    public String displayResetPasswordForm(@RequestParam(required = false) String token,
                                           Model model){
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            model.addAttribute("error", "Could not find password reset token");
        } else if (resetToken.isExpired()) {
            model.addAttribute("error", "Token has expired, please request a new password reset.");
        } else {
            model.addAttribute("token", resetToken.getToken());
        }
        return "reset-password";
    }

    @PostMapping
    @Transactional
    public String handlePasswordReset(@ModelAttribute("passwordResetForm") @Valid PasswordResetDto resetDto,
                                      BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".passwordResetForm", result);
            redirectAttributes.addFlashAttribute("passwordResetForm", resetDto);
            return "redirect:/reset-password?token=" + resetDto.getToken();
        }

        PasswordResetToken token = tokenRepository.findByToken(resetDto.getToken());
        User user = token.getUser();
        String updatePassword = passwordEncoder.encode(resetDto.getPassword());
        userService.updatePassword(updatePassword, user.getId());
        tokenRepository.delete(token);

        return "redirect:/login?resetSuccess";
    }
}

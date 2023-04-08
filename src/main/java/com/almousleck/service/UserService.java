package com.almousleck.service;

import com.almousleck.domain.User;
import com.almousleck.domain.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);
    User register(UserRegistrationDto registration);
    void updatePassword(String password, Long id);
}

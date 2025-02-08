package com.strawberry.production.auth.service;

import com.strawberry.production.auth.dto.ResetPasswordRequestDto;
import org.springframework.security.core.Authentication;


public interface AuthService {
    String generateToken(Authentication authentication);

    String resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
    
}

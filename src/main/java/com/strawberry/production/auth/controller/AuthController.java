package com.strawberry.production.auth.controller;

import com.strawberry.production.auth.dto.AuthResponseDto;
import com.strawberry.production.auth.dto.LoginRequestDto;
import com.strawberry.production.auth.dto.ResetPasswordRequestDto;
import com.strawberry.production.auth.entity.UserAuth;
import com.strawberry.production.auth.service.AuthService;
import com.strawberry.production.response.Response;
import jakarta.servlet.http.Cookie;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Log
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        logger.info("Login attempt for user: {}", loginRequestDto.getUsername());
        logger.debug("Login request details: {}", loginRequestDto);
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserAuth userDetails = (UserAuth) authentication.getPrincipal();
            String token = authService.generateToken(authentication);

            AuthResponseDto response = new AuthResponseDto();
            response.setUser(userDetails.getUser());
            response.setMessage("Login success");
            response.setToken(token);

            Cookie cookie = new Cookie("sid", token);
            cookie.setMaxAge(24 * 60 * 60);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; Path=/; HttpOnly");

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(Response.successResponse(response).getBody());
        }catch (AuthenticationException error) {
            logger.warn("Failed login attempt for user: {} - Reason: {}",
                    loginRequestDto.getUsername(), error.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.failedResponse("Invalid username or password").getBody());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response<Object>> resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        try{
            return Response.successResponse("Reset password success", authService.resetPassword(resetPasswordRequestDto));
        } catch (Exception e) {
            return Response.failedResponse("Reset password failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Cookie cookie = new Cookie("sid", null);
        cookie.setMaxAge(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; Path=/; HttpOnly; Max-Age=0");
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(Response.successResponse("Logout success").getBody());
    }
}

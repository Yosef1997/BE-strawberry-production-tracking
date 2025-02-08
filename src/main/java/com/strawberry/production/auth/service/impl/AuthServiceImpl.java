package com.strawberry.production.auth.service.impl;

import com.strawberry.production.auth.dto.ResetPasswordRequestDto;
import com.strawberry.production.auth.service.AuthService;
import com.strawberry.production.exceptions.notFoundException.NotFoundException;
import com.strawberry.production.users.entity.Users;
import com.strawberry.production.users.repository.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    public AuthServiceImpl(JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(24, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        Users user = usersRepository.findByUsername(resetPasswordRequestDto.getUsername()).orElseThrow(() -> new NotFoundException("User with username: " + resetPasswordRequestDto.getUsername() + " not found"));
        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
        usersRepository.save(user);
        return "Reset password success. Please try to login again";
    }

}

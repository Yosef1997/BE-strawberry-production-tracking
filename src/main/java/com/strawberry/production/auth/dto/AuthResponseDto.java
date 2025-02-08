package com.strawberry.production.auth.dto;

import com.strawberry.production.users.entity.Users;
import lombok.Data;

@Data
public class AuthResponseDto {
    private Users user;
    private String message;
    private String token;
}

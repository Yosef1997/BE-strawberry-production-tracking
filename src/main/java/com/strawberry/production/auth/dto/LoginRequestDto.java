package com.strawberry.production.auth.dto;

import lombok.Data;
@Data
public class LoginRequestDto {
    private String username;
    private String password;
}

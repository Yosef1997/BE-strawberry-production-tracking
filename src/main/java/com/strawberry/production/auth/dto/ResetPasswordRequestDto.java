package com.strawberry.production.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String username;
    private String newPassword;
}

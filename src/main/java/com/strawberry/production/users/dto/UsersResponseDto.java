package com.strawberry.production.users.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class UsersResponseDto {
    private Long id;
    private String name;
    private String username;
    private String role;
    private Instant createdAt;
    private Instant deletedAt;
}

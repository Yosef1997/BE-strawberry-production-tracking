package com.strawberry.production.users.dto;

import com.strawberry.production.users.entity.Users;
import lombok.Data;

@Data
public class UsersRequestDto {
    private String name;
    private String username;
    private String password;
    private Users.Role role;

    public Users toEntity() {
        Users user = new Users();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}

package com.strawberry.production.users.service;

import com.strawberry.production.users.dto.UsersRequestDto;
import com.strawberry.production.users.dto.UsersResponseDto;
import com.strawberry.production.users.entity.Users;

import java.util.List;

public interface UsersService {
    List<UsersResponseDto> getAllUsers();
    UsersResponseDto getDetailUser(String username);
    Users getDetailUserId(Long id);
    UsersResponseDto createPic(UsersRequestDto usersRequestDto);
    UsersResponseDto updatePic(UsersRequestDto usersRequestDto);
    String deletePic(UsersRequestDto usersRequestDto);
}

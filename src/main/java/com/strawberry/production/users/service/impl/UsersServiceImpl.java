package com.strawberry.production.users.service.impl;

import com.strawberry.production.exceptions.notFoundException.NotFoundException;
import com.strawberry.production.users.dto.UsersRequestDto;
import com.strawberry.production.users.dto.UsersResponseDto;
import com.strawberry.production.users.entity.Users;
import com.strawberry.production.users.repository.UsersRepository;
import com.strawberry.production.users.service.UsersService;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Log
public class UsersServiceImpl implements UsersService {
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsersResponseDto> getAllUsers() {
        return usersRepository.findAll().stream().map(this::mapToUsersResponseDto).toList();
    }

    @Override
    public UsersResponseDto getDetailUser(String username) {
        Users usernameExists = usersRepository.findByUsername(username).orElse(null);
        if (usernameExists != null) {
            return mapToUsersResponseDto(usernameExists);
        }
        return null;
    }

    @Override
    public Users getDetailUserId(Long id) {
        return usersRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public UsersResponseDto createPic(UsersRequestDto usersRequestDto) {
        Users newPic = usersRequestDto.toEntity();
        newPic.setPassword(passwordEncoder.encode(usersRequestDto.getPassword()));
        Users savedPic = usersRepository.save(newPic);
        return mapToUsersResponseDto(savedPic);
    }

    @Override
    public UsersResponseDto updatePic(UsersRequestDto usersRequestDto) {
        Users user = usersRepository.findByUsername(usersRequestDto.getUsername()).orElseThrow(() -> new NotFoundException("User with username: " + usersRequestDto.getUsername() + " not found"));
        user.setName(usersRequestDto.getName());
        user.setUsername(usersRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(usersRequestDto.getPassword()));
        user.setRole(usersRequestDto.getRole());
        Users updatedUser = usersRepository.save(user);
        return mapToUsersResponseDto(updatedUser);
    }

    @Override
    public String deletePic(UsersRequestDto usersRequestDto) {
        logger.info("Delete attempt for user: {}", usersRequestDto.getUsername());
        Users user = usersRepository.findByUsername(usersRequestDto.getUsername()).orElseThrow(() -> new NotFoundException("User with username: " + usersRequestDto.getUsername() + " not found"));
        if (user != null){
            user.setDeletedAt(Instant.now());
            usersRepository.save(user);
            return "User with username: " + usersRequestDto.getUsername() + " have deleted successfully";
        }
        return "Delete user failed";
    }

    public UsersResponseDto mapToUsersResponseDto(Users user) {
        UsersResponseDto response = new UsersResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setCreatedAt(user.getCreatedAt());
        response.setDeletedAt(user.getDeletedAt());
        return response;
    }
}

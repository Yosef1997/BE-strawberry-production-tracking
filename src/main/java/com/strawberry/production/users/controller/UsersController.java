package com.strawberry.production.users.controller;

import com.strawberry.production.response.Response;
import com.strawberry.production.users.dto.UsersRequestDto;
import com.strawberry.production.users.dto.UsersResponseDto;
import com.strawberry.production.users.service.UsersService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Log
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<Response<List<UsersResponseDto>>> getAllUsers() {
        return Response.successResponse("All users", usersService.getAllUsers());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response<UsersResponseDto>> signup(@RequestBody UsersRequestDto signupRequestDto) {
        return Response.successResponse("Create PIC success", usersService.createPic(signupRequestDto));
    }

    @PutMapping("/change-pic-data")
    public ResponseEntity<Response<UsersResponseDto>> changePicData(@RequestBody UsersRequestDto updateUsersRequestDto) {
        return Response.successResponse("Update PIC data success", usersService.updatePic(updateUsersRequestDto));
    }

    @DeleteMapping
    public ResponseEntity<Response<String>> deletePic(@RequestBody UsersRequestDto deleteUsersRequestDto) {
        return Response.successResponse("Delete PIC data success", usersService.deletePic(deleteUsersRequestDto));
    }
}

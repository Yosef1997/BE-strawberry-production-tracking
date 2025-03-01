package com.strawberry.production.reject.controller;

import com.strawberry.production.reject.dto.RejectRequestDto;
import com.strawberry.production.reject.dto.RejectResponseDto;
import com.strawberry.production.reject.service.RejectService;

import com.strawberry.production.response.Response;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reject")
@Validated
@Log
public class RejectController {
    private final RejectService rejectService;

    public RejectController(RejectService rejectService) {
        this.rejectService = rejectService;
    }

    @GetMapping
    public ResponseEntity<Response<List<RejectResponseDto>>> getAllRejects() {
        return Response.successResponse("All rejects", rejectService.getAllRejects());
    }

    @PostMapping
    public ResponseEntity<Response<RejectResponseDto>> createReject(@RequestBody RejectRequestDto rejectRequestDto) {
        return Response.successResponse("Create reject data success", rejectService.createReject(rejectRequestDto));
    }

    @PutMapping
    public ResponseEntity<Response<RejectResponseDto>> editReject(@RequestBody RejectRequestDto rejectRequestDto) {
        return Response.successResponse("Edit reject data success", rejectService.editReject(rejectRequestDto));
    }
}

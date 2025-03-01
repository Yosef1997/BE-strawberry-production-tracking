package com.strawberry.production.yield.controller;

import com.strawberry.production.response.Response;
import com.strawberry.production.yield.dto.YieldRequestDto;
import com.strawberry.production.yield.dto.YieldResponseDto;
import com.strawberry.production.yield.service.YieldService;
import com.strawberry.production.yield.service.impl.YieldServiceImpl;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/yield")
@Validated
@Log
public class YieldController {
    private final YieldService yieldService;

    public YieldController(YieldService yieldService) {
        this.yieldService = yieldService;
    }

    @GetMapping
    public ResponseEntity<Response<List<YieldResponseDto>>> getAllYields() {
        return Response.successResponse("All yields", yieldService.getAllYields());
    }

    @PostMapping
    public ResponseEntity<Response<YieldResponseDto>> createYield(@RequestBody YieldRequestDto yieldRequestDto) {
        return Response.successResponse("Create yield data success", yieldService.createYield(yieldRequestDto));
    }

    @PutMapping
    public ResponseEntity<Response<YieldResponseDto>> editYield(@RequestBody YieldRequestDto yieldRequestDto) {
        return Response.successResponse("Edit yield data success", yieldService.editYield(yieldRequestDto));
    }
}

package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.food.FoodService;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodStatusRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {
    private final FoodService foodService;

    @Description(
        "음식 등록"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> createFood(
        @RequestPart(value = "request") CreateFoodRequestDto request,
        @RequestPart(value = "image", required = false) MultipartFile images
    ) {
        foodService.createFood(request, images);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식 수정"
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateFood(
        @PathVariable("id") UUID id,
        @RequestPart(value = "request") UpdateFoodRequestDto update,
        @RequestPart(value = "image") MultipartFile images
    ) {
        foodService.updateFood(id, update, images);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식 상태 변경"
    )
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateFoodStatus(
        @PathVariable("id") UUID id,
        @RequestBody UpdateFoodStatusRequestDto update
    ) {
        foodService.updateFoodStatus(id, update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식 숨김 상태 변경"
    )
    @PatchMapping("/{id}/display-status")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> toggleDisplay(@PathVariable("id") UUID id) {
        foodService.toggleDisplay(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식 삭제"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> deleteFood(@PathVariable("id") UUID id) {
        foodService.deleteFoodWithImage(id);
        return ResponseEntity.noContent().build();
    }
}
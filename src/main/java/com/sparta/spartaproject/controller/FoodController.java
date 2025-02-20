package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.food.FoodService;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodStatusRequestDto;
import com.sparta.spartaproject.dto.response.FoodDetailDto;
import com.sparta.spartaproject.dto.response.FoodDto;
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
        "음식 전체 조회 (관리자, 운영자)"
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<FoodDto> getAllFoods(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(foodService.getAllFoods(page, sortDirection));
    }

    @Description(
        "음식점 별 음식 조회"
    )
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<FoodDto> getAllFoodsForStore(
        @PathVariable("storeId") UUID storeId,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(foodService.getAllFoodsForStore(storeId, page, sortDirection));
    }

    @Description(
        "음식 상세 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<FoodDetailDto> getFood(
        @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(foodService.getFood(id));
    }

    @Description(
        "음식 등록"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> createFood(
        @RequestPart(value = "request") CreateFoodRequestDto request,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        foodService.createFood(request, image);
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
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        foodService.updateFood(id, update, image);
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
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
}
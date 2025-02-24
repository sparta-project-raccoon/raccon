package com.sparta.spartaproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.common.pageable.PageableConfig;
import com.sparta.spartaproject.domain.food.FoodService;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodStatusRequestDto;
import com.sparta.spartaproject.dto.response.FoodDetailDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {
    private final FoodService foodService;
    private final PageableConfig pageableConfig;

    @Description(
        "음식 등록"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> createFood(
        @RequestPart(value = "request") String requestJson,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) throws JsonProcessingException {
        CreateFoodRequestDto request = new ObjectMapper().readValue(requestJson, CreateFoodRequestDto.class);
        foodService.createFood(request, image);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식 전체 조회 (관리자, 운영자)"
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Page<FoodDetailDto>> getAllFoods(
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(foodService.getAllFoods(customPageable));
    }

    @Description(
        "음식점 별 음식 조회"
    )
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<Page<FoodDetailDto>> getAllFoodsForStore(
        @PathVariable("storeId") UUID storeId,
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(foodService.getAllFoodsForStore(storeId, customPageable));
    }

    @Description(
        "업주 - 음식점 별 음식 조회 "
    )
    @GetMapping("/my/stores/{storeId}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Page<FoodDetailDto>> getAllFoodsForStoreByOwner(
        @PathVariable("storeId") UUID storeId,
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(foodService.getAllFoodsForStoreByOwner(storeId, customPageable));
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
        "음식 수정"
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateFood(
        @PathVariable("id") UUID id,
        @RequestPart(value = "request") String updateJson,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) throws JsonProcessingException {
        UpdateFoodRequestDto update = new ObjectMapper().readValue(updateJson, UpdateFoodRequestDto.class);
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
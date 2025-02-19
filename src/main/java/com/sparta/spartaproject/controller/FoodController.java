package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.food.FoodService;
import com.sparta.spartaproject.domain.food.Status;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.response.FoodDetailDto;
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
    public ResponseEntity<FoodDetailDto> createFood(
        @RequestPart(value = "data") CreateFoodRequestDto request,
        @RequestPart(value = "image")MultipartFile image) {
        return ResponseEntity.ok(foodService.createFoodWithImage(request, image));
    }

    @Description(
        "음식 수정"
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<FoodDetailDto> updateFood(
        @PathVariable("id") UUID id,
        @RequestPart(value = "data") UpdateFoodRequestDto update,
        @RequestPart(value = "image")MultipartFile image) {
        return ResponseEntity.ok(foodService.updateFoodWithImage(id, update, image));
    }

    @Description(
        "음식 상태 변경"
    )
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<FoodDetailDto> updateFoodStatus(
            @PathVariable("id") UUID id,
            @RequestParam("status") Status status) {
        return ResponseEntity.ok(foodService.updateFoodStatus(id, status));
    }

    @Description(
        "음식 숨김 상태 변경"
    )
    @PatchMapping("/{id}/display-status")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<String> toggleFoodDisplay(@PathVariable("id") UUID id) {

        if(foodService.toggleIsDisplayed(id)){
            return ResponseEntity.ok("isDisplayed: true");
        }
        return ResponseEntity.ok("isDisplayed: false");
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

package com.sparta.spartaproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.common.pageable.PageableConfig;
import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.dto.request.ConfirmStoreRequestDto;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreStatusRequestDto;
import com.sparta.spartaproject.dto.response.StoreByCategoryDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;
    private final PageableConfig pageableConfig;

    @Description(
        "음식점 생성하기"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> createStore(
        @RequestPart(value = "request") String requestJson,
        @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList
    ) throws JsonProcessingException {
        CreateStoreRequestDto request = new ObjectMapper().readValue(requestJson, CreateStoreRequestDto.class);
        storeService.createStore(request, imageList);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식점 전체 조회하기"
    )
    @GetMapping
    public ResponseEntity<Page<StoreDetailDto>> getStores(
        @RequestParam(required = false, defaultValue = "1") Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sortDirection,
        @RequestParam(value = "name", required = false, defaultValue = "") String name
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(storeService.getStores(customPageable, name));
    }

    @Description(
        "음식점 상세 조회하기"
    )
    @GetMapping("/{id}")
    public ResponseEntity<StoreDetailDto> getStore(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(storeService.getStore(id));
    }

    @Description(
        "카테고리별 음식점 조회하기"
    )
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Page<StoreByCategoryDto>> getStoresByCategory(
        @PathVariable("categoryId") UUID categoryId,
        @RequestParam(required = false, defaultValue = "1") Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(storeService.getStoresByCategory(customPageable, categoryId));
    }

    @Description(
        "내 음식점 목록 조회하기"
    )
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<StoreDetailDto> getMyStores() {
        return ResponseEntity.ok(storeService.getMyStores());
    }

    @Description(
        "음식점 정보 수정하기"
    )
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateStore(
        @PathVariable("id") UUID id,
        @RequestPart("request") String updateJson,
        @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList
    ) throws JsonProcessingException {
        UpdateStoreRequestDto update = new ObjectMapper().readValue(updateJson, UpdateStoreRequestDto.class);
        storeService.updateStore(id, update, imageList);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식점 상태 변경하기 - 영업 시작, 영업 종료, 브레이크 타임"
    )
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateStoreStatus(@PathVariable("id") UUID id, @RequestBody UpdateStoreStatusRequestDto update) {
        storeService.updateStoreStatus(id, update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식점 삭제하기"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<String> deleteStore(@PathVariable("id") UUID id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "Master, Manager - 승인되지 않은 가게 전체 조회하기"
    )
    @GetMapping("/unconfirmed")
    public ResponseEntity<Page<StoreDetailDto>> getUnconfirmedStores(
        @RequestParam(required = false, defaultValue = "1") Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(storeService.getUnconfirmedStores(customPageable));
    }

    @Description(
        "Master, Manager - 가게 승인하기"
    )
    @PatchMapping("/confirmed")
    public ResponseEntity<Void> confirmedStore(@RequestBody ConfirmStoreRequestDto update) {
        storeService.confirmedStore(update);
        return ResponseEntity.ok().build();
    }
}
package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreStatusRequestDto;
import com.sparta.spartaproject.dto.response.StoreByCategoryDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;

    @Description(
        "음식점 생성하기"
    )
    @PostMapping
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> createStore(@RequestBody CreateStoreRequestDto request) {
        storeService.createStore(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "음식점 전체 조회하기"
    )
    @GetMapping
    public ResponseEntity<StoreDto> getStores(
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "") String name
    ) {
        return ResponseEntity.ok(storeService.getStores(page, name));
    }

    @Description(
        "음식점 상세 조회하기"
    )
    @GetMapping("/{id}")
    public ResponseEntity<StoreDetailDto> getStore(@PathVariable UUID id) {
        return ResponseEntity.ok(storeService.getStore(id));
    }

    @Description(
        "카테고리별 음식점 조회"
    )
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<StoreByCategoryDto> getStoresByCategory(
        @PathVariable UUID categoryId,
        @RequestParam(required = false, defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(storeService.getStoresByCategory(page, categoryId));
    }

    @Description(
        "내 음식점 조회하기"
    )
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<List<StoreDetailDto>> getMyStores() {
        return ResponseEntity.ok(storeService.getMyStores());
    }

    @Description(
        "가게 정보 수정하기"
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateStore(@PathVariable UUID id, @RequestBody UpdateStoreRequestDto update) {
        storeService.updateStore(id, update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "가게 상태 변경하기 - 영업 시작, 영업 종료, 브레이크 타임"
    )
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateStoreStatus(@PathVariable UUID id, @RequestBody UpdateStoreStatusRequestDto update) {
        storeService.updateStoreStatus(id, update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "가게 삭제하기"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'MASTER', 'MANAGER')")
    public ResponseEntity<String> deleteStore(@PathVariable UUID id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok().build();
    }
}
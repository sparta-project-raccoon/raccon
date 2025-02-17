package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.store.Status;
import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import com.sparta.spartaproject.dto.response.StoreDetailDto;
import com.sparta.spartaproject.dto.response.StoreSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService storeService;
    private final StoreImageService storeImageService;


    // 음식점 등록 (권한 check)
    @PostMapping
    public ResponseEntity<String> createStore(@RequestBody CreateStoreRequestDto request) {
        storeService.createStore(request);
        return ResponseEntity.ok("가게 등록 완료");
    }

    // 음식점 상세 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailDto> getStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeService.getStore(storeId));
    }

    // 전체 음식점 조회
    @GetMapping
    public ResponseEntity<Page<StoreSummaryDto>> getAllStores(Pageable pageable) {
        return ResponseEntity.ok(storeService.getAllStores(pageable));
    }

    // 카테고리 별 조회
    @GetMapping(params = "categoryId")
    public ResponseEntity<Page<StoreSummaryDto>> getAllStoresByCategoryId(@RequestParam UUID categoryId, Pageable pageable){
        return ResponseEntity.ok(storeService.getAllStoresByCategoryId(categoryId, pageable));
    }


    // 내 음식점 조회 (권한 check)
    @GetMapping("/my")
    public ResponseEntity<Page<StoreSummaryDto>> getMyStores(Pageable pageable){
        return ResponseEntity.ok(storeService.getMyStores(pageable));
    }


    // 음식점 정보 수정 (권한 check)
    @PutMapping("/{storeId}")
    public ResponseEntity<String> updateStore(@PathVariable UUID storeId, @RequestBody UpdateStoreRequestDto update){
        storeService.updateStore(storeId, update);
        return ResponseEntity.ok("가게 정보 수정 완료");
    }

    // 음식점 상태 변경 (권한 check)
    @PatchMapping("/{storeId}/status")
    public ResponseEntity<String> updateStoreStatus(@PathVariable UUID storeId, @RequestParam Status status){
        storeService.updateStoreStatus(storeId, status);
        return ResponseEntity.ok("가게 상태 정보 수정 완료");
    }

    // 음식점 삭제 (권한 check)
    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> deleteStore(@PathVariable UUID storeId){
        storeService.deleteStore(storeId);
        return ResponseEntity.ok().build();
    }

    // 음식점 검색
    @GetMapping(params ="searchWord")
    public ResponseEntity<Page<StoreSummaryDto>> searchStores(@RequestParam String searchWord, Pageable pageable){
        return ResponseEntity.ok(storeService.searchStores(searchWord, pageable));
    }

    // 음식점 이미지 저장 (권한 : 해당 음식점 사장님, 관리자)
    @PostMapping("/image/{storeId}")
    public ResponseEntity<List<ImageInfoDto>> saveStoreImages(
            @PathVariable UUID storeId,
            @RequestParam("images") List<MultipartFile> images) {
        return ResponseEntity.ok(storeImageService.saveStoreImages(storeId, images));
    }

    // 음식점 이미지 조회
    @GetMapping("/image/{storeId}")
    public ResponseEntity<List<ImageInfoDto>> getStoreImages(@PathVariable UUID storeId) {
        return ResponseEntity.ok(storeImageService.getStoreImages(storeId));
    }

    // 이미지 삭제 (권한 : 해당 음식점 사장님, 관리자)
    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID imageId) {
        storeImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

}

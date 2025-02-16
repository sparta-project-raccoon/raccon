package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.image.ImageService;
import com.sparta.spartaproject.domain.image.Type;
import com.sparta.spartaproject.dto.request.SaveImageRequestDto;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    // 이미지 저장 (다중) (권한 : 해당 음식점 사장님, 관리자)
    @PostMapping
    public ResponseEntity<List<ImageInfoDto>> saveImages(@ModelAttribute SaveImageRequestDto request) {
        return ResponseEntity.ok(imageService.saveImages(request));
    }

    // 이미지 조회
    @GetMapping
    public ResponseEntity<List<ImageInfoDto>> getAllImages(@RequestParam("type") Type type, @RequestParam("id") UUID id) {
        return ResponseEntity.ok(imageService.getAllImages(type, id));
    }

    // 이미지 삭제 (권한 : 해당 음식점 사장님, 관리자)
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

}

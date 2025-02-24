package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Description(
        "관리자 - 이미지 등록"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> uploadImage(
        @RequestParam("entityType") EntityType entityType,
        @RequestParam("entityId") UUID entityId,
        @RequestPart("imageList") List<MultipartFile> imageList
    ) {
        imageService.uploadImageByManager(entityId, entityType, imageList);
        return ResponseEntity.ok().build();
    }

    @Description(
        "관리자 - 이미지 등록"
    )
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateImagesByManager(
        @RequestParam("entityType") EntityType entityType,
        @RequestParam("entityId") UUID entityId,
        @RequestPart("imageList") List<MultipartFile> imageList
    ) {
        imageService.updateImagesByEntityByManager(entityId, entityType, imageList);
        return ResponseEntity.ok().build();
    }

    @Description(
        "관리자 - 이미지 삭제"
    )
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> deleteImagesByManager(
        @RequestParam("entityType") EntityType entityType,
        @RequestParam("entityId") UUID entityId
    ) {
        imageService.deleteAllImagesByEntity(entityId, entityType);
        return ResponseEntity.ok().build();
    }
}

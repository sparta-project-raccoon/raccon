package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
import lombok.RequiredArgsConstructor;
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

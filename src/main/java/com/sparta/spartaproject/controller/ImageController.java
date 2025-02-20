package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/{entityType}/{entityId}")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public ResponseEntity<String> uploadImage(
            @PathVariable EntityType entityType,
            @PathVariable UUID entityId,
            @RequestParam("image") MultipartFile image) {
        String imageUrl = imageService.uploadImage(entityId, entityType, image);
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/{entityType}/{entityId}")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public ResponseEntity<Void> deleteImage(
            @PathVariable EntityType entityType,
            @PathVariable UUID entityId) {
        imageService.deleteAllImagesByEntity(entityId, entityType);
        return ResponseEntity.noContent().build();
    }
}

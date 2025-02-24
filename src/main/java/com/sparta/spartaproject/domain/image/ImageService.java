package com.sparta.spartaproject.domain.image;

import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    /**
     * 특정 엔티티에 이미지 업로드
     */
    @Transactional
    public String uploadImage(UUID entityId, EntityType entityType, MultipartFile imageFile) {
        try {
            String imageUrl = s3Uploader.uploadImageFile(imageFile, entityType);
            Image image = Image.builder()
                    .entityId(entityId)
                    .entityType(entityType)
                    .imageUrl(imageUrl)
                    .isDeleted(false)
                    .build();
            imageRepository.save(image);
            return imageUrl;
        } catch (Exception e) {
            log.error("이미지 업로드 중 오류 발생: ", e);
            throw new BusinessException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    /**
     * 특정 엔티티의 모든 이미지 삭제 (s3 영구 삭제, DB 영구 삭제)
     */
    @Transactional
    public void deleteAllImagesByEntity(UUID entityId, EntityType entityType) {
        List<Image> imageList = imageRepository.findByEntityIdAndEntityType(entityId, entityType);

        if (!imageList.isEmpty()) {
            for (Image image : imageList) {

                s3Uploader.deleteImageFile(image.getImageUrl());
                log.info("삭제된 url : {}", image.getImageUrl());

                imageRepository.delete(image);
            }
        }
    }

    /**
     * 특정 엔티티의 모든 이미지 조회 (URL 리스트)
     */
    @Transactional(readOnly = true)
    public List<String> getImageUrlByEntity(UUID entityId, EntityType entityType) {
        return imageRepository.findByEntityIdAndEntityType(entityId, entityType).stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
    }

    public void uploadImageByManager(UUID entityId, EntityType entityType, List<MultipartFile> imageList) {
        imageList.forEach(image -> {
            uploadImage(entityId, entityType, image);
        });
    }

    public void updateImagesByEntityByManager(UUID entityId, EntityType entityType, List<MultipartFile> imageList) {
        deleteAllImagesByEntity(entityId, entityType);
        imageList.forEach(image -> {
            uploadImage(entityId, entityType, image);
        });
    }
}

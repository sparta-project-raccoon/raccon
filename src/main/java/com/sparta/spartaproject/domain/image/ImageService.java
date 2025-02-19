package com.sparta.spartaproject.domain.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    /**
     * 특정 엔티티에 이미지 업로드
     */
    public String uploadImage(UUID entityId, EntityType entityType, MultipartFile imageFile) {
        String imageUrl = s3Uploader.uploadImageFile(imageFile, entityType);
        Image image = Image.builder()
                .entityId(entityId)
                .entityType(entityType)
                .imageUrl(imageUrl)
                .isDeleted(false)
                .build();
        imageRepository.save(image);
        return imageUrl;
    }


    /**
     * 특정 엔티티의 모든 이미지 삭제 (s3 영구 삭제, DB 존재(삭제 여부 true, 삭제 일시 O))
     */
    public void deleteAllImagesByEntity(UUID entityId, EntityType entityType) {
        List<Image> imageList = imageRepository.findByEntityIdAndEntityType(entityId, entityType);

        for (Image image : imageList) {
            s3Uploader.deleteImageFile(image.getImageUrl()); // s3에서 삭제

            image.delete(); // DB의 삭제 여부 & 삭제 일시 변경

//            imageRepository.delete(image); // DB에서 영구 삭제
        }
    }

    /**
     * 특정 엔티티의 모든 이미지 조회 (URL 리스트)
     */
    public List<String> getImageUrlByEntity(UUID entityId, EntityType entityType) {
        return imageRepository.findByEntityIdAndEntityType(entityId, entityType).stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
    }

    /**
     * 특정 엔티티의 이미지 일괄 수정 (기존 이미지 삭제 후 새로 업로드)
     */
    public List<String> replaceAllImages(UUID entityId, EntityType entityType, List<MultipartFile> newImages) {
        deleteAllImagesByEntity(entityId, entityType);

        return newImages.stream()
                .map(image ->uploadImage(entityId, entityType, image))
                .collect(Collectors.toList());
    }

}

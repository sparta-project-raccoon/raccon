package com.sparta.spartaproject.domain.image;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private static final String BASE_PATH = "images"; //

    public String uploadImageFile(MultipartFile imageFile, EntityType entityType) {
        try{
            String uuid = UUID.randomUUID().toString();
            String originalFilename = imageFile.getOriginalFilename();
            String imageFileName = BASE_PATH + "/" + entityType + "/" + uuid + "-" + originalFilename;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(imageFile.getSize());
            objectMetadata.setContentType(imageFile.getContentType());

            amazonS3.putObject(new PutObjectRequest(bucketName, imageFileName, imageFile.getInputStream(), objectMetadata));
            log.info("S3에 업로드된 파일 : {}", imageFileName);

            return amazonS3.getUrl(bucketName, imageFileName).toString();
        }catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패");
        }
    }

    public void deleteImageFile(String imageUrl) {
        try {
            String fileKey = extractKeyFromUrl(imageUrl);
            String decodedKey = URLDecoder.decode(fileKey, StandardCharsets.UTF_8);
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, decodedKey));
            log.info("S3에서 삭제된 파일: {}", decodedKey);
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", imageUrl, e);
            throw new RuntimeException("파일 삭제 실패");
        }
    }

    private String extractKeyFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.indexOf(BASE_PATH+"/"));
    }



}

package com.sparta.spartaproject.test;

import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.S3Uploader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class S3UploaderTest {

    @Autowired
    private S3Uploader s3Uploader;

    @Test
    public void testUploadImageFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test-image.jpg", "image/jpeg", "test-content".getBytes());

        String uploadedUrl = s3Uploader.uploadImageFile(file, EntityType.REVIEW);
        assertNotNull(uploadedUrl);
        System.out.println("Uploaded URL: " + uploadedUrl);
    }
    @Test
    public void testDeleteImageFile() {
        String uploadedUrl = "https://dydybucket.s3.ap-northeast-2.amazonaws.com/images/REVIEW/b348a1ed-359b-43a6-961a-96b4708b4b4b-test-image.jpg";
        //  파일 삭제 테스트
        s3Uploader.deleteImageFile(uploadedUrl);
        System.out.println("Deleted URL: " + uploadedUrl);

//        //  삭제 확인 (S3에서 직접 확인 필요)
//        assertThat(isDeleted(uploadedUrl)).isTrue();
    }
}
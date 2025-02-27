package com.sparta.spartaproject.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.domain.image.*;
import com.sparta.spartaproject.domain.store.*;
import com.sparta.spartaproject.domain.user.UserRepository;
import com.sparta.spartaproject.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
class ImageServiceTest {
    private static final Logger log = LoggerFactory.getLogger(StoreImageServiceTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreImageService storeImageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StoreImageRepository storeImageRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private S3Uploader s3Uploader;

    private UUID entityId;
    private EntityType entityType;
    private MockMultipartFile mockImage;
    private Image image;
    @Autowired
    private UserRepository userRepository;


    /**
     * ✅ 이미지 업로드 테스트
     */
    @Test
    @DisplayName("음식점 등록 성공 테스트")
    void saveStoreImages_Success() throws Exception {
        // given
        entityId = UUID.fromString("497cea4c-03a7-423f-808a-111ac5d2601f");
        entityType = EntityType.STORE;

        File file = new File("C:/Users/SUNJIN/OneDrive/Pictures/피나치공.jpeg");

        // FileInputStream을 사용하여 파일 읽기
        FileInputStream input = new FileInputStream(file);

        // MockMultipartFile 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "imageList",               // 필드 이름
                file.getName(),            // 파일 이름
                "image/jpeg",              // 파일 타입
                input                      // 파일 데이터 (InputStream)
        );


        // when & then

        String url = imageService.uploadImage(entityId, entityType, mockFile);
        System.out.println("entityId: " + entityId+", url: " + url);
    }

//    /**
//     * ✅ 특정 엔티티의 모든 이미지 조회 테스트
//     */
//    @Test
//    void getImageUrlByEntity_Success() {
//        // Given
//        when(imageRepository.findByEntityIdAndEntityType(entityId, entityType))
//                .thenReturn(List.of(image));
//
//        // When
//        List<String> imageUrls = imageService.getImageUrlByEntity(entityId, entityType);
//
//        // Then
//        assertThat(imageUrls).isNotEmpty();
//        assertThat(imageUrls).contains(image.getImageUrl());
//        verify(imageRepository, times(1)).findByEntityIdAndEntityType(entityId, entityType);
//    }
//
//    /**
//     * ✅ 특정 엔티티의 모든 이미지 삭제 테스트
//     */
//    @Test
//    void deleteAllImagesByEntity_Success() {
//        // Given
//        when(imageRepository.findByEntityIdAndEntityType(entityId, entityType))
//                .thenReturn(List.of(image));
//
//        // When
//        imageService.deleteAllImagesByEntity(entityId, entityType);
//
//        // Then
//        verify(s3Uploader, times(1)).deleteImageFile(image.getImageUrl()); // S3 삭제 확인
//        verify(imageRepository, times(1)).delete(image); // DB 삭제 확인
//    }
}

package com.sparta.spartaproject.storeTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.domain.store.*;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserRepository;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
class StoreImageServiceTest {

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
    private UserRepository userRepository;

    private UUID storeId;
    private UUID imageId;


    @BeforeEach
    void setUp() {
        // 테스트용 유저 생성
        User user = userRepository.findById(5L).get();

        // 테스트용 음식점 생성
        Store store = storeService.getStoreById(UUID.fromString("a5e398ef-90ab-4571-9a25-fd60dfd8a627"));
        storeId = store.getId();
    }

    @Test
    @DisplayName("음식점 이미지 저장 성공 테스트")
    void saveStoreImages_Success() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile(
                "images", "test-image1.jpg", "image/jpeg", "file-content-1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "images", "test-image2.jpg", "image/jpeg", "file-content-2".getBytes()
        );
        // when & then
        mockMvc.perform(multipart("/api/stores/image/{storeId}", storeId)
                        .file(file1)
                        .file(file2)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("음식점 이미지 조회 테스트")
    void getStoreImages_Success() {
        // given
        // 위의 storeId 사용

        // when
        List<ImageInfoDto> images = storeImageService.getStoreImages(storeId);

        // then
        images.forEach(imageInfoDto -> {
            log.info("{}", imageInfoDto.path());
        });
        assertThat(images).isNotEmpty();
    }

    @Test
    @DisplayName("음식점 이미지 삭제 테스트")
    void deleteImage_Success() {
        // given
        UUID imageId = UUID.fromString("3c7f6977-6f43-4db6-b142-da44e88b89a5");

        // when
        storeImageService.deleteImage(imageId);

        // then
        StoreImage deletedImage = storeImageRepository.findById(imageId).orElse(null);
        assertThat(deletedImage).isNotNull();
        assertThat(deletedImage.getIsDeleted()).isTrue();
    }
}
package com.sparta.spartaproject.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.domain.food.Status;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreImageRepository;
import com.sparta.spartaproject.domain.store.StoreImageService;
import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserRepository;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
public class FoodServiceTest {
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

    private User user;
    private Store store;
    private UUID storeId;
    private MockMultipartFile mockImage;
    private CreateFoodRequestDto request;
    private UpdateFoodRequestDto update;

    @BeforeEach
    void setUp() {
        // 테스트용 유저
        user = userRepository.findById(5L).get();

        // 테스트용 음식점
        store = storeService.getStoreById(UUID.fromString("a5e398ef-90ab-4571-9a25-fd60dfd8a627"));
        storeId = store.getId();

        // 테스트용 이미지
        mockImage = new MockMultipartFile("image", "image_1.jpg", "image/jpeg", "file-content".getBytes());
    }

    @Test
    @DisplayName("음식 등록 성공 테스트")
    void saveFoodWithImage_Success() throws Exception {
        // given
        // 음식 등록 요청 DTO 생성
        request = new CreateFoodRequestDto(
                storeId,
                "마라탕",
                13000,
                "맛있음요",
                Status.PREPARING
        );
        // 요청 DTO를 JSON으로 변환
        String requestJson = objectMapper.writeValueAsString(request);


        // when & then
        mockMvc.perform(multipart("/api/foods")
                .file(mockImage) // 이미지 파일 추가
                .file(new MockMultipartFile(
                        "data", "", "application/json", requestJson.getBytes()
                ))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("마라탕"))
                .andExpect(jsonPath("$.price").value(13000))
                .andExpect(jsonPath("$.description").value("맛있음요"));
    }

    @Test
    @DisplayName("음식 정보 수정 성공 테스트")
    void updateFoodWithImage_Success() throws Exception {
        // given
        // 음식 수정 요청 DTO 생성
        update = new UpdateFoodRequestDto(
                "쌀국수",
                8000,
                "아고 맛있어",
                Status.SALE
        );
        // 요청 DTO를 JSON으로 변환
        String requestJson = objectMapper.writeValueAsString(update);


        UUID foodId = UUID.fromString("97dfb163-08e0-4352-a822-5915349ddc6b");
        // when & then
        mockMvc.perform(multipart("/api/foods/{id}", foodId)
                        .file(new MockMultipartFile(
                                "data", "", "application/json", requestJson.getBytes()
                        ))
                        .file(mockImage) // 이미지 파일 추가
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT"); // 메소드 : PUT으로 설정
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("쌀국수"))
                .andExpect(jsonPath("$.price").value(8000))
                .andExpect(jsonPath("$.description").value("아고 맛있어"));

    }

    @Test
    @DisplayName("음식 상태 변경 성공 테스트")
    void updateFoodStatus_Success() throws Exception {
        // 요청 데이터 설정 (Status 변경)
        UUID foodId = UUID.fromString("97dfb163-08e0-4352-a822-5915349ddc6b");
        String newStatus = Status.SOLD_OUT.name();

        mockMvc.perform(patch("/api/foods/" + foodId)
                        .param("status", newStatus)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(newStatus));
    }

    @Test
    @DisplayName("음식 표시 상태 변경 API - 성공 ")
    void toggleFoodDisplay_success() throws Exception {
        // Given
        UUID foodId = UUID.fromString("9d271b0b-a67d-4a17-82f6-cc2c407c2fbd");

        // When & Then
        mockMvc.perform(patch("/api/foods/{id}/display", foodId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("isDisplayed: false"));
    }



    @Test
    @DisplayName("음식 삭제 성공 테스트")
    void deleteFoodSuccess() throws Exception {
        UUID foodId = UUID.fromString("9d271b0b-a67d-4a17-82f6-cc2c407c2fbd");

        mockMvc.perform(delete("/api/foods/" + foodId))
                .andExpect(status().isNoContent());
    }
}

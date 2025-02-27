package com.sparta.spartaproject.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.food.FoodService;
import com.sparta.spartaproject.domain.food.Status;
import com.sparta.spartaproject.domain.review.Review;
import com.sparta.spartaproject.domain.review.ReviewService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
public class FoodImageTest {
    private static final Logger log = LoggerFactory.getLogger(StoreImageServiceTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewService reviewService;


    private UUID storeId;
    private UUID imageId;
    private UUID reviewId;
    private UUID orderId;
    @Autowired
    private FoodService foodService;

    @BeforeEach
    void setUp() {
        // 테스트용 유저
        User user = userRepository.findById(7L).get();

        // 테스트용 리뷰
        Review review = reviewService.getReviewByIdAndIsDeletedIsFalse(UUID.fromString("d1ec3e9f-0e96-4f72-b505-0afef0044166"));
        reviewId = review.getId();

        storeId = UUID.fromString("a5e398ef-90ab-4571-9a25-fd60dfd8a627");
        orderId = UUID.fromString("09ed4e9c-c991-4cdf-b18f-04cbd317f4fa");

    }

    @Test
    @DisplayName("음식 등록 테스트")
    @WithMockUser(authorities = {"OWNER"})
    void createFood_Success() throws Exception {
        CreateFoodRequestDto request = new CreateFoodRequestDto(UUID.fromString("24dfbe9f-49ec-4a17-b58e-1a5569a6d9a1"),"해물파전", 17000,"바삭한 해물파전", Status.SALE);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        MockMultipartFile requestPart = new MockMultipartFile(
                "request",  // @RequestPart("request") 와 일치해야 함
                "",
                "application/json",
                requestJson.getBytes(StandardCharsets.UTF_8)
        );

        File file = new File("C:/Users/SUNJIN/OneDrive/Pictures/해물파전.jpg");

        // FileInputStream을 사용하여 파일 읽기
        FileInputStream input = new FileInputStream(file);

        // MockMultipartFile 생성
        MockMultipartFile image = new MockMultipartFile(
                "image",               // 필드 이름
                file.getName(),            // 파일 이름
                "image/jpeg",              // 파일 타입
                input                      // 파일 데이터 (InputStream)
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/foods")
                        .file(requestPart)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("음식 수정 테스트")
    @WithMockUser(authorities = {"OWNER"})
    void updateFood_Success() throws Exception {
        UUID foodId = UUID.fromString("ebfc042d-0c34-485f-ba93-7ca1f72779a5");
        UpdateFoodRequestDto request = new UpdateFoodRequestDto("김치 킹왕 칼국수", 9800, "왕이에요", Status.SALE);
        String requestJson = new ObjectMapper().writeValueAsString(request);
        MockMultipartFile requestPart = new MockMultipartFile(
                "request",
                "",
                "application/json",
                requestJson.getBytes(StandardCharsets.UTF_8)
        );

        MockMultipartFile image = new MockMultipartFile(
                "image", "test-image3.jpg", "image/jpeg", "file-content-1".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/foods/" + foodId)
                        .file(requestPart)
//                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("음식 삭제 테스트")
    @WithMockUser(authorities = {"OWNER"})
    void deleteFood_Success() throws Exception {
        UUID foodId = UUID.fromString("b2c3269a-c807-4c93-958e-c02a166d1ea8");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/foods/" + foodId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("음식 상세 조회 성공 테스트")
    void getFood_Success() throws Exception {
        UUID foodid = UUID.fromString("c61bdadb-db76-45a6-bef2-003ac34ffa36");
        Food food = foodService.getFoodById(foodid);
        mockMvc.perform(get("/api/foods/" + foodid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.id").value(foodid.toString())) // 음식 ID 확인
                .andExpect(jsonPath("$.name").value(food.getName())) ;// 음식 이름 확인
    }


    @Test
    @DisplayName("전체 음식 목록 조회 - 기본값 (page=1, sortDirection=asc)")
    void getAllFoods_DefaultValues_Success() throws Exception {
//        User user = userRepository.findById(1L).get();
        mockMvc.perform(get("/api/foods")
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // HTTP 200 OK 검증
    }

    @Test
    @DisplayName("음식점 별 음식 목록 조회 - 기본값 (page=1, sortDirection=asc)")
    void getAllFoodsByStore_DefaultValues_Success() throws Exception {
        UUID storeid = UUID.fromString("24dfbe9f-49ec-4a17-b58e-1a5569a6d9a1");
        Store store = storeService.getStoreById(storeid);
        mockMvc.perform(get("/api/foods/stores/" + storeid)
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.length()").value(3)); // 음식 개수 검증

    }
}



package com.sparta.spartaproject.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.domain.review.Review;
import com.sparta.spartaproject.domain.review.ReviewService;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserRepository;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.request.UpdateReviewRequestDto;
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

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
public class ReviewImageTest {

    private static final Logger log = LoggerFactory.getLogger(StoreImageServiceTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewService reviewService;

    private UUID storeId;
    private UUID imageId;
    private UUID reviewId;
    private UUID orderId;

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
    @DisplayName("리뷰 이미지 저장 성공 테스트")
    void saveReviewImages_Success() throws Exception {
        // given

        UUID storeid = UUID.fromString("a5e398ef-90ab-4571-9a25-fd60dfd8a627");
        UUID orderid = UUID.fromString("09ed4e9c-c991-4cdf-b18f-04cbd317f4fa");

        CreateReviewRequestDto request = new CreateReviewRequestDto(storeid,orderid,"연예인 맛집인 듯;", 2);
        String requestJson = new ObjectMapper().writeValueAsString(request);

        MockMultipartFile requestPart = new MockMultipartFile(
                "request",  // @RequestPart("request") 와 일치해야 함
                "",
                "application/json",
                requestJson.getBytes(StandardCharsets.UTF_8)
        );

        File file = new File("C:/Users/SUNJIN/OneDrive/Pictures/파묘.jpg");

        // FileInputStream을 사용하여 파일 읽기
        FileInputStream input = new FileInputStream(file);

        // MockMultipartFile 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "imageList",               // 필드 이름
                file.getName(),            // 파일 이름
                "image/jpeg",              // 파일 타입
                input                      // 파일 데이터 (InputStream)
        );

        MockMultipartFile file1 = new MockMultipartFile(
                "imageList", "test-image3.jpg", "image/jpeg", "file-content-1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "imageList", "test-image4.jpg", "image/jpeg", "file-content-2".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/reviews")
                        .file(requestPart) // JSON 데이터
                        .file(mockFile)
//                        .file(file1) // 이미지 파일 1
//                        .file(file2) // 이미지 파일 2
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("리뷰 상세 조회 성공 테스트")
    void getReview_Success() throws Exception {
        User user = userRepository.findById(1L).get();
        mockMvc.perform(get("/api/reviews/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.id").value(reviewId.toString())) // 리뷰 ID 확인
                .andExpect(jsonPath("$.userId").value(user.getId().toString())) // 유저 ID 확인
                .andExpect(jsonPath("$.storeId").value(storeId.toString())) // 음식점 ID 확인
                .andExpect(jsonPath("$.orderId").value(orderId.toString())) // 주문 ID 확인
                .andExpect(jsonPath("$.content").value("넘 맛있어요")) // 리뷰 내용 확인
                .andExpect(jsonPath("$.rating").value(4)); // 별점 확인

    }

    @Test
    @DisplayName("내가 작성한 리뷰 목록 조회 - 기본값 (page=1, sortDirection=asc)")
    void getMyReviewsWithImages_DefaultValues_Success() throws Exception {
        User user = userRepository.findById(1L).get();
        mockMvc.perform(get("/api/reviews/my")
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.length()").value(2)) // 리뷰 개수 검증
                .andExpect(jsonPath("$[0].userId").value(user.getId().toString())) // 본인 리뷰인지 확인
                .andExpect(jsonPath("$[0].storeId").value(storeId.toString())) // 음식점 ID 확인
                .andExpect(jsonPath("$[0].orderId").value(orderId.toString())) // 주문 ID 확인
                .andExpect(jsonPath("$[0].content").value("넘 맛있어요")) // 첫 번째 리뷰 내용 확인
                .andExpect(jsonPath("$[0].rating").value(4)); // 첫 번째 리뷰 별점 확인

    }

    @Test
    @DisplayName("전체 리뷰 목록 조회 - 기본값 (page=1, sortDirection=asc)")
    void getReviewsWithImages_DefaultValues_Success() throws Exception {
//        User user = userRepository.findById(1L).get();
        mockMvc.perform(get("/api/reviews")
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // HTTP 200 OK 검증
    }

    @Test
    @DisplayName("리뷰 이미지 수정 성공 테스트")
    void updateReviewImages_Success() throws Exception {
        // given
        UUID reviewid = UUID.fromString("d1ec3e9f-0e96-4f72-b505-0afef0044166");

        UpdateReviewRequestDto update = new UpdateReviewRequestDto("고기가 디이박 맛있어요 짱짱", 5);
        String requestJson = new ObjectMapper().writeValueAsString(update);

        MockMultipartFile updatePart = new MockMultipartFile(
                "request",  // @RequestPart("request") 와 일치해야 함
                "",
                "application/json",
                requestJson.getBytes(StandardCharsets.UTF_8)
        );

        File file = new File("C:/Users/SUNJIN/OneDrive/Pictures/파묘.jpg");

        // FileInputStream을 사용하여 파일 읽기
        FileInputStream input = new FileInputStream(file);

        // MockMultipartFile 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "imageList",               // 필드 이름
                file.getName(),            // 파일 이름
                "image/jpeg",              // 파일 타입
                input                      // 파일 데이터 (InputStream)
        );


        MockMultipartFile file1 = new MockMultipartFile(
                "imageList", "test-image3.jpg", "image/jpeg", "file-content-1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "imageList", "test-image4.jpg", "image/jpeg", "file-content-2".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/reviews/" + reviewid)
                        .file(updatePart)
                        .file(mockFile)
//                        .file(file1)
//                        .file(file2)
                        .with(request -> {
                            request.setMethod("PATCH");  // ✅ PATCH 요청으로 변경
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("리뷰 삭제 성공 - 이미지 포함")
    void deleteReviewWithImages_Success() throws Exception {

        UUID revewid = UUID.fromString("773c4336-1c0f-48d6-8845-faeefeb1bb4b");
        // When: 리뷰 삭제 요청 수행
        mockMvc.perform(delete("/api/reviews/{id}", revewid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("가게 전체 리뷰 목록 조회 - 기본값 (page=1, sortDirection=asc)")
    void getReviewsWithImagesForStore_DefaultValues_Success() throws Exception {
//        User user = userRepository.findById(1L).get();
        UUID storid = UUID.fromString("a5e398ef-90ab-4571-9a25-fd60dfd8a627");
        mockMvc.perform(get("/api/reviews/stores/"+storid)
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // HTTP 200 OK 검증
    }

}

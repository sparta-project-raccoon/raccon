package com.sparta.spartaproject.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sparta.spartaproject.domain.store.*;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserRepository;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
public class StoreImageTest {

    private static final Logger log = LoggerFactory.getLogger(StoreImageServiceTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
    }


    @Test
    @DisplayName("음식점 등록 성공 테스트")
    void saveStoreImages_Success() throws Exception {
        // given
        User user = userRepository.findById(5L).get();
        Long userId = user.getId();
        List<UUID> categories= List.of(
            UUID.fromString("f02bfc54-94ad-4218-ac76-0a6519405dd7"), // 치킨
            UUID.fromString("0b79a312-99a4-4152-acab-08fabcf6c4a4") // 피자
        );

        CreateStoreRequestDto request = new CreateStoreRequestDto(
                categories,"피자나라 치킨공주","서울시 서대문구", Status.BEFORE_OPEN,
                "02-345-6789", "을매나 맛나게요", "11:00","23:00", ClosedDays.AllDay);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO-8601 형식 유지

        String requestJson = objectMapper.writeValueAsString(request);

        MockMultipartFile requestPart = new MockMultipartFile(
                "request",  // @RequestPart("request") 와 일치해야 함
                "",
                "application/json",
                requestJson.getBytes(StandardCharsets.UTF_8)
        );

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

        MockMultipartFile file1 = new MockMultipartFile(
                "imageList", "test-image3.jpg", "image/jpeg", "file-content-1".getBytes()
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "imageList", "test-image4.jpg", "image/jpeg", "file-content-2".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/api/stores")
                        .file(requestPart) // JSON 데이터
                        .file(mockFile)
//                        .file(file1) // 이미지 파일 1
//                        .file(file2) // 이미지 파일 2
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("전체 음식점 목록 조회 - 기본값 (page=1, sortDirection=asc, name='')")
    void getStores_DefaultValues_Success() throws Exception {
//        User user = userRepository.findById(1L).get();
        mockMvc.perform(get("/api/stores")
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.stores.size()").value(10));
    }

    @Test
    @DisplayName("전체 음식점 목록 조회 - 기본값 (page=1, sortDirection=asc, name='피자')")
    void getStores_Success() throws Exception {
//        User user = userRepository.findById(1L).get();
        mockMvc.perform(get("/api/stores")
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .param("name", "피자")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.stores.size()").value(3));
    }

    @Test
    @DisplayName("음식점 상세 조회 성공 테스트")
    void getStore_Success() throws Exception {
        UUID storeid = UUID.fromString("f2c2965b-2234-4855-8afe-ad45fc0c28ec");
        mockMvc.perform(get("/api/stores/" + storeid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.id").value(storeid.toString())) // 음식 ID 확인
                .andExpect(jsonPath("$.name").value("피자나라 치킨공주")) // 유저 ID 확인
                .andExpect(jsonPath("$.address").value("서울시 서대문구")); // 별점 확인

    }

    @Test
    @DisplayName("카테고리 별 음식점 목록 조회 - 기본값 (page=1, sortDirection=asc, name='피자')")
    void getStoresByCategory_Success() throws Exception {
        UUID categoryId = UUID.fromString("deb88fa7-ebdf-4a01-8e3b-ae3d475faf45"); // 한식

        mockMvc.perform(get("/api/stores/categories/"+categoryId)
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.stores.size()").value(4));
    }

    @Test
    @DisplayName("내 음식점 목록 조회 - 기본값 (page=1, sortDirection=asc)")
    void getMyStores_Success() throws Exception {

        mockMvc.perform(get("/api/stores/my")
                        .param("page", "1")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // HTTP 200 OK 검증
                .andExpect(jsonPath("$.stores.size()").value(4));
    }

    @Test
    @DisplayName("음식점 수정 성공 테스트")
    void updateStore_Success() throws Exception {
        // given
        User user = userRepository.findById(5L).get();
        Long userId = user.getId();
        UUID storeId = UUID.fromString("f2c2965b-2234-4855-8afe-ad45fc0c28ec");
        List<UUID> categories= List.of(
                UUID.fromString("f02bfc54-94ad-4218-ac76-0a6519405dd7"), // 치킨
                UUID.fromString("0b79a312-99a4-4152-acab-08fabcf6c4a4") // 피자
        );

        CreateStoreRequestDto request = new CreateStoreRequestDto(
                categories,"피자나라 치킨공주","서울시 중구", Status.OPEN,
                "02-345-9999", "을매나 맛있게요~ 냠냠", "11:00", "23:00", ClosedDays.AllDay);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO-8601 형식 유지

        String requestJson = objectMapper.writeValueAsString(request);

        MockMultipartFile requestPart = new MockMultipartFile(
                "request",  // @RequestPart("request") 와 일치해야 함
                "",
                "application/json",
                requestJson.getBytes(StandardCharsets.UTF_8)
        );

        File file = new File("C:/Users/SUNJIN/OneDrive/Pictures/피자치킨.jpg");

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
        mockMvc.perform(multipart("/api/stores/"+storeId)
                        .file(requestPart) // JSON 데이터
                        .file(mockFile)
//                        .file(file1) // 이미지 파일 1
//                        .file(file2) // 이미지 파일 2
                        .with(req -> {
                            req.setMethod("PATCH");  // ✅ PATCH 요청으로 변경
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(username = "testUser", authorities = {"OWNER"})
    @DisplayName("음식점 삭제 성공 - 이미지 포함")
    void deleteStore_Success() throws Exception {

        UUID storeId = UUID.fromString("f2c2965b-2234-4855-8afe-ad45fc0c28ec");
        // When: 음식점 삭제 요청 수행
        mockMvc.perform(delete("/api/stores/{id}", storeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}

package com.sparta.spartaproject.serviceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.domain.user.*;
import com.sparta.spartaproject.domain.verify.Code;
import com.sparta.spartaproject.domain.verify.VerifyService;
import com.sparta.spartaproject.dto.request.*;
import com.sparta.spartaproject.dto.response.TokenDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false) // 자동 롤백 방지
public class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authorization;
    @Autowired
    private VerifyService verifyService;

    @BeforeEach
    void setUp() {
        // given
//        LoginRequestDto request = new LoginRequestDto("tttest", "0000"); //Customer
        LoginRequestDto request = new LoginRequestDto("kdy", "Abc1234!"); // Master
        // when 1
        TokenDto tokenDto =userService.login(request);

        // then 1
        log.info("로그인된 Token의 grantType: {}",tokenDto.grantType());
        log.info("로그인된 Token의 accessToken: {}",tokenDto.accessToken());

        authorization = tokenDto.grantType()+" "+tokenDto.accessToken();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUp_success() {
        // given
        SignUpRequestDto request = new SignUpRequestDto(
                "tttest",
                "0000",
                "kdoy0300@gmail.com",
                "tttest",
                "010-1234-5678",
                "서울특별시 중구"
        );

        // when
        userService.signUp(request);

        // then
        User savedUser = userRepository.findByUsername("tttest").orElseThrow();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("tttest");
        assertThat(savedUser.getEmail()).isEqualTo("kdoy0300@gmail.com");
        assertThat(passwordEncoder.matches("0000", savedUser.getPassword())).isTrue(); // 비밀번호 암호화 확인
        log.info("등록된 User의 Status : {}",savedUser.getStatus());
        log.info("등록된 User의 Role : {}",savedUser.getRole());
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void signUp_failure() {
        // given
        SignUpRequestDto request = new SignUpRequestDto(
                "kdy",
                "0000",
                "kdoy030@gmail.com",
                "kdy",
                "010-1234-5678",
                "서울특별시 중구"
        );

        // when
        userService.signUp(request);

        // then
        User savedUser = userRepository.findByUsername("kdy").orElseThrow();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("kdy");
        assertThat(savedUser.getEmail()).isEqualTo("kdoy030@gmail.com");
        assertThat(passwordEncoder.matches("0000", savedUser.getPassword())).isTrue(); // 비밀번호 암호화 확인
        log.info("등록된 User의 Status : {}",savedUser.getStatus());
        log.info("등록된 User의 Role : {}",savedUser.getRole());
    }

    @Test
    @DisplayName("인증 코드 전송 & 확인")
    void verify_success() throws Exception {
        // given
        SendCodeRequestDto request = new SendCodeRequestDto("kdoy030@gmail.com"); // 이메일 넣으면 됨!
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // 인증코드 확인 API 호출
        mockMvc.perform(MockMvcRequestBuilders.post("/api/verify/send-code") // ✅ POST 요청으로 변경
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))  // ✅ JSON 본문 추가
                .andExpect(status().isOk());

        // given
        ConfirmCodeRequestDto confirmRequest = new ConfirmCodeRequestDto("test12@example.com", "565380"); // 인증코드 넣으면 됨!
        requestJson = new ObjectMapper().writeValueAsString(confirmRequest);

        // 인증코드 확인 API 호출
        mockMvc.perform(MockMvcRequestBuilders.post("/api/verify/confirm-code") // POST 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))  // JSON 본문 추가
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("아이디 찾기 테스트")
    void findUsername_success() throws Exception {
        // given
        FindUsernameRequestDto request = new FindUsernameRequestDto("kdoy030@gmail.com", "kdy"); // email, name
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // 아이디 찾기 API 호출
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/find-username") // POST 요청
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))  // JSON 본문 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("kdy")) // 사용자 role 확인
        ;
    }

    @Test
    @DisplayName("회원 로그인 후 조회 테스트")
    void getUserInfo_success() throws Exception {
        // 인증된 상태에서 사용자 정보 조회 API 호출
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                        .header("Authorization", authorization)  // ✅ Authorization 헤더 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("kdoy030@gmail.com")) // 사용자 email 확인
                .andExpect(jsonPath("$.name").value("kdy")) // 사용자 이름 확인
                .andExpect(jsonPath("$.role").value("CUSTOMER")) // 사용자 role 확인
                .andExpect(jsonPath("$.status").value("COMPLETE")) // 사용자 role 확인
                ;
    }

    @Test
    @DisplayName("회원 로그인 후 비밀번호 변경 테스트")
    void changePassword_success() throws Exception {
        UpdatePasswordRequestDto request = new UpdatePasswordRequestDto("Abc1234!");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // 인증된 상태에서 사용자 비밀 번호 변경 API 호출
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/change-password")
                        .header("Authorization", authorization)  // ✅ Authorization 헤더 추가
                        .content(requestJson) // JSON 본문 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("매니저 계정 등록 테스트")
    void updateRoleManager_success() throws Exception {
        UpdateRoleManagerRequestDto request = new UpdateRoleManagerRequestDto("TWFuYWdlciDqtoztlZwg7KSY6528");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // 인증된 상태에서 사용자 비밀 번호 변경 API 호출
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/role/manager")
                        .header("Authorization", authorization)  // ✅ Authorization 헤더 추가
                        .content(requestJson) // JSON 본문 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마스터 계정 등록 테스트")
    void updateRoleMaster_success() throws Exception {
        UpdateRoleMasterRequestDto request = new UpdateRoleMasterRequestDto("TWFzdGVyIOq2jO2VnCDspJjrnbw=");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // 인증된 상태에서 사용자 비밀 번호 변경 API 호출
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/role/master")
                        .header("Authorization", authorization)  // ✅ Authorization 헤더 추가
                        .content(requestJson) // JSON 본문 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잠금 회원 활성화 테스트")
    void activeUser_success() throws Exception {
        ActiveUserRequestDto request = new ActiveUserRequestDto("userTest2");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // 인증된 상태에서 사용자 비밀 번호 변경 API 호출
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/active")
                        .header("Authorization", authorization)  // ✅ Authorization 헤더 추가
                        .content(requestJson) // JSON 본문 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



}

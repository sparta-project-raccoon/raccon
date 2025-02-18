package com.sparta.spartaproject.storeTest;

import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserRepository;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.SignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Rollback(false) // 자동 롤백 방지
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // 테스트용 유저 데이터 초기화
//        userRepository.deleteAll();
    }

    @Test
    @DisplayName("✅ 회원가입 성공 테스트")
    void signUp_success() {
        // given
        SignUpRequestDto request = new SignUpRequestDto(
                "testUser",
                "123!",
                "test@example.com",
                "doyeon",
                "010-1234-5678",
                "서울특별시 중구"
        );

        // when
        userService.signUp(request);

        // then
        User savedUser = userRepository.findByUsername("testUser").orElseThrow();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("testUser");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getRole()).isEqualTo(Role.CUSTOMER);
        assertThat(savedUser.getStatus()).isNotNull();
        assertThat(passwordEncoder.matches("123!", savedUser.getPassword())).isTrue(); // 비밀번호 암호화 확인
    }

    @Test
    @DisplayName(" 이미 존재하는 아이디로 회원가입 실패")
    void signUp_fail_duplicateUsername() {
        // given
        SignUpRequestDto request1 = new SignUpRequestDto(
                "testUser",
                "password123!",
                "test1@example.com",
                "김철수",
                "010-5678-1234",
                "서울특별시 강남구"
        );
        userService.signUp(request1);

        // when
        SignUpRequestDto request2 = new SignUpRequestDto(
                "testUser", // 동일한 username
                "password456!",
                "test2@example.com",
                "이영희",
                "010-7777-8888",
                "서울특별시 서초구"
        );

        // then
        assertThrows(ResponseStatusException.class, () -> userService.signUp(request2));
    }

    @Test
    @DisplayName("❌ 이미 존재하는 이메일로 회원가입 실패")
    void signUp_fail_duplicateEmail() {
        // given
        SignUpRequestDto request1 = new SignUpRequestDto(
                "userA",
                "password123!",
                "duplicate@example.com", // 동일한 이메일
                "박지성",
                "010-3333-4444",
                "서울특별시 성동구"
        );
        userService.signUp(request1);

        // when
        SignUpRequestDto request2 = new SignUpRequestDto(
                "userB",
                "password789!",
                "duplicate@example.com", // 동일한 이메일
                "손흥민",
                "010-9999-0000",
                "서울특별시 동작구"
        );

        // then
        assertThrows(ResponseStatusException.class, () -> userService.signUp(request2));
    }
}
package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.FindUsernameRequestDto;
import com.sparta.spartaproject.dto.request.LoginRequestDto;
import com.sparta.spartaproject.dto.request.SignUpRequestDto;
import com.sparta.spartaproject.dto.request.UpdatePasswordRequestDto;
import com.sparta.spartaproject.dto.response.FindUsernameDto;
import com.sparta.spartaproject.dto.response.TokenDto;
import com.sparta.spartaproject.dto.response.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Description(
        "회원가입"
    )
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto request) {
        userService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "로그인"
    )
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @Description(
        "아이디 찾기"
    )
    @PostMapping("/find-username")
    public ResponseEntity<FindUsernameDto> findUsername(@RequestBody FindUsernameRequestDto request) {
        return ResponseEntity.ok(userService.findUsername(request));
    }

    @Description(
        "사용자 정보 조회"
    )
    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getUserInfo() {
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @Description(
        "사용자 비밀번호 변경"
    )
    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody UpdatePasswordRequestDto request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}
package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.*;
import com.sparta.spartaproject.dto.response.FindUsernameDto;
import com.sparta.spartaproject.dto.response.TokenDto;
import com.sparta.spartaproject.dto.response.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<Void> signUp(@Validated @RequestBody SignUpRequestDto request) {
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
    public ResponseEntity<Void> changePassword(@Validated @RequestBody UpdatePasswordRequestDto request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "잠금 회원 활성화"
    )
    @PostMapping("/active")
    public ResponseEntity<Void> activeUser(@RequestBody ActiveUserRequestDto request) {
        userService.activeUser(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "매니저 계정 등록하기"
    )
    @PostMapping("/role/manager")
    public ResponseEntity<Void> updateRoleManager(@RequestBody UpdateRoleManagerRequestDto request) {
        userService.updateRoleManager(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "마스터 계정 등록하기"
    )
    @PostMapping("/role/master")
    public ResponseEntity<Void> updateRoleMaster(@RequestBody UpdateRoleMasterRequestDto request) {
        userService.updateRoleMaster(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "사장 계정 등록하기"
    )
    @PostMapping("/role/owner")
    public ResponseEntity<Void> updateRoleOwner(@RequestBody UpdateRoleOwnerRequestDto request) {
        userService.updateRoleOwner(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "사업자 등록 번호로 사장 계정 인증 받기 (사업자 등록 번호 조회 API 사용)"
    )
    @PostMapping("/role/owner/business-num")
    public ResponseEntity<Void> updateRoleOwnerByBusinessNum(@RequestBody VarifyBusinessNumRequestDto request) {
        userService.updateRoleOwnerByBusinessNum(request);
        return ResponseEntity.ok().build();
    }
}
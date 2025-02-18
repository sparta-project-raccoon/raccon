package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.verify.VerifyService;
import com.sparta.spartaproject.dto.request.ConfirmCodeRequestDto;
import com.sparta.spartaproject.dto.request.SendCodeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verify")
public class VerifyController {
    private final VerifyService verifyService;

    @Description(
        "인증 코드 보내기"
    )
    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@Validated @RequestBody SendCodeRequestDto sendCodeRequestDto) {
        verifyService.sendCode(sendCodeRequestDto);
        return ResponseEntity.ok().build();
    }

    @Description(
        "인증 코드 확인하기"
    )
    @PostMapping("/confirm-code")
    public ResponseEntity<Void> confirmCode(@Validated @RequestBody ConfirmCodeRequestDto confirmCodeRequestDto) {
        verifyService.confirmCode(confirmCodeRequestDto);
        return ResponseEntity.ok().build();
    }
}
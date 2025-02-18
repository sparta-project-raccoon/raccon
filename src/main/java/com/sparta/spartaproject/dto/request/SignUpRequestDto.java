package com.sparta.spartaproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
    @NotBlank(message = "빈 값이 될 수 없습니다.")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 4글자 이상 10글자 이하로 구성되어야 합니다.")
    String username,

    @NotBlank(message = "빈 값이 될 수 없습니다.")
    @Size(min = 8, max = 15, message = "비밀번호는 8글자 이상 15글자 이하로 구성되어야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,15}$",
        message = "비밀번호는 소문자, 대문자, 특수문자 최소 1개 이상 포함되어야 합니다."
    )
    String password,

    @Email
    @NotBlank(message = "빈 값이 될 수 없습니다.")
    String email,

    String name,
    String phone,
    String address
) {
}
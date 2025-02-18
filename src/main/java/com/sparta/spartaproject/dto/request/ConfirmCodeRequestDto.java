package com.sparta.spartaproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmCodeRequestDto(
    @NotBlank(message = "빈 값이 될 수 없습니다.")
    @Email
    @Size(min = 6, max = 100, message = "이메일은 100글자 이상 작성할 수 없습니다.")
    String email,
    String code
) {
}
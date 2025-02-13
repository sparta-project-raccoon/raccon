package com.sparta.spartaproject.dto.request;

public record SignUpRequestDto(
    String username,
    String password,
    String email,
    String name,
    String phone,
    String address
) {
}
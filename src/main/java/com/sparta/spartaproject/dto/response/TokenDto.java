package com.sparta.spartaproject.dto.response;

public record TokenDto(
    String grantType,
    String accessToken,
    String refreshToken
) {
}
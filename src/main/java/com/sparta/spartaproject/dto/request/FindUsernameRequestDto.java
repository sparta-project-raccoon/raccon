package com.sparta.spartaproject.dto.request;

public record FindUsernameRequestDto(
    String email,
    String name
) {
}
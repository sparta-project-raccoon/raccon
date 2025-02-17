package com.sparta.spartaproject.dto.request;

public record UpdateReviewRequestDto(
    String content,
    Integer rating
) {
}
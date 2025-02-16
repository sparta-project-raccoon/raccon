package com.sparta.spartaproject.dto.request;

import java.util.UUID;

public record CreateReviewRequestDto(
    UUID storeId,
    UUID orderId,
    String content,
    Integer rating
) {
}
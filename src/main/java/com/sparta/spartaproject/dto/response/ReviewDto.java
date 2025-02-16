package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record ReviewDto(
    UUID id,
    Long userId,
    UUID storeId,
    UUID orderId,
    String content,
    Integer rating
) {
}
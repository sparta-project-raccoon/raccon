package com.sparta.spartaproject.dto.response;

import java.util.List;
import java.util.UUID;

public record ReviewDto(
    UUID id,
    Long userId,
    UUID storeId,
    UUID orderId,
    String content,
    Integer rating,
    List<String> imageUrlList // 리뷰 이미지 URL
) {
}
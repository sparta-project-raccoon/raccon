package com.sparta.spartaproject.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GeminiHistoryResponseDto(
    UUID id,
    Long userId,
    UUID storeId,
    String requestText,
    String responseText,
    Integer statusCode,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
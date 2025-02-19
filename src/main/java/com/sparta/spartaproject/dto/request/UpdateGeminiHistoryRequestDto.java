package com.sparta.spartaproject.dto.request;

import java.util.UUID;

public record UpdateGeminiHistoryRequestDto(
    UUID storeId,
    String requestText,
    String responseText,
    Integer statusCode
) {
}
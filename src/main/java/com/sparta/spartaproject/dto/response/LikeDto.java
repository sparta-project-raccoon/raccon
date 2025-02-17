package com.sparta.spartaproject.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record LikeDto(
    UUID id,
    Long userId,
    UUID storeId,
    Boolean isDeleted,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.order.PayMethod;

import java.time.LocalDateTime;
import java.util.UUID;

public record PayHistoryDetailDto(
    UUID id,
    UUID orderId,
    UUID storeId,
    String storeName,
    Integer totalPrice,
    PayMethod payMethod,
    LocalDateTime createdAt
) {
}
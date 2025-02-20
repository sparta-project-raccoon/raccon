package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OnlyOrderDto(
        UUID orderId,
        OrderStatus status,
        Integer totalPrice,
        String storeName,
        String foodName,
        Integer totalFoodCnt,
        LocalDateTime createdAt
) {
}

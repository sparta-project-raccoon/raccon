package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDto(
    UUID id,
    UUID storeId,
    List<FoodQtySummaryDto> foods,
    OrderStatus status,
    Integer totalPrice,
    Integer totalFoodCount,
    LocalDateTime createdAt
) {
}
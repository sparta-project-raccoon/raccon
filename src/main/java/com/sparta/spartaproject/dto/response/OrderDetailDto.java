package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.order.OrderMethod;
import com.sparta.spartaproject.domain.order.OrderStatus;
import com.sparta.spartaproject.domain.order.PayMethod;

import java.util.List;
import java.util.UUID;

public record OrderDetailDto(
    UUID id,
    Long userId,
    UUID storeId,
    String storeName,
    Integer totalPrice,
    String request,
    OrderMethod orderMethod,
    PayMethod payMethod,
    String address,
    OrderStatus status,
    Integer totalFoodCount,
    List<FoodQtySummaryDto> foods
) {
}
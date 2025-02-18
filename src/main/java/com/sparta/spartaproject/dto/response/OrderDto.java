package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.order.OrderStatus;

import java.util.UUID;

public record OrderDto(
        UUID orderId,
        OrderStatus status,
        Integer totalPrice,
        String storeName
) {
}

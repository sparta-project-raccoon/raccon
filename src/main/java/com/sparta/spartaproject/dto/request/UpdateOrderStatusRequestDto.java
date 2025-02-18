package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.order.OrderStatus;

import java.util.UUID;

public record UpdateOrderStatusRequestDto(
        UUID orderId,
        OrderStatus orderStatus
) {

}

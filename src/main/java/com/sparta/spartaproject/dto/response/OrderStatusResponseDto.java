package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record OrderStatusResponseDto(
        UUID orderId,
        String orderStatus
) {
}

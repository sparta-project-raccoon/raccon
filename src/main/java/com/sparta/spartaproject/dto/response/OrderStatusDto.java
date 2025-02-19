package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record OrderStatusDto(
        UUID orderId,
        String orderStatusDescription
) {
}

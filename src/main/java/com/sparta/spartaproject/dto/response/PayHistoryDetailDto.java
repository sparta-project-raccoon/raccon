package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record PayHistoryDetailDto(
        UUID orderId,
        String shopName,
        Integer totalPrice,
        String paymentMethod
) {
}

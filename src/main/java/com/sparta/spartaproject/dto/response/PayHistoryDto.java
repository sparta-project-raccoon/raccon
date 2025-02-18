package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record PayHistoryDto(
        UUID orderId,
        String shopName,
        Integer totalPrice
) {
}

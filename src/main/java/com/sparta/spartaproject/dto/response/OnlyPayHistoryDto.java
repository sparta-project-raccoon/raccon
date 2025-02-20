package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record OnlyPayHistoryDto(
        UUID orderId,
        String shopName,
        Integer totalPrice
) {
}

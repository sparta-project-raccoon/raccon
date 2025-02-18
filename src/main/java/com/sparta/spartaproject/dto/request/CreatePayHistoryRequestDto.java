package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.order.PaymentMethod;

import java.util.UUID;

public record CreatePayHistoryRequestDto(
        UUID orderId,
        PaymentMethod paymentMethod,
        UUID storeId,
        Integer totalPrice
) {
}

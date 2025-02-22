package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.order.PayMethod;

import java.util.UUID;

public record CreatePayHistoryRequestDto(
    UUID orderId,
    PayMethod payMethod,
    UUID storeId
) {
}
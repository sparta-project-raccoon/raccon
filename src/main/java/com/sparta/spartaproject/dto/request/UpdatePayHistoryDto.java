package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.order.PayMethod;
import lombok.Builder;

@Builder
public record UpdatePayHistoryDto(
    PayMethod payMethod,
    Integer totalPrice
) {
}
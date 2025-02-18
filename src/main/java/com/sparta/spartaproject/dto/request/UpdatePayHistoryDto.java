package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.order.PaymentMethod;
import lombok.Builder;

@Builder
public record UpdatePayHistoryDto(
        PaymentMethod paymentMethod,
        Integer totalPrice
) {

}

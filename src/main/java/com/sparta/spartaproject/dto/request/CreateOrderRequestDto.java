package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.order.OrderMethod;
import com.sparta.spartaproject.domain.order.PaymentMethod;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDto(
        UUID storeId,
        List<CreateFoodOrderRequestDto> foods,
        OrderMethod orderMethod,
        PaymentMethod payMethod,
        String request,
        String address
) {

}

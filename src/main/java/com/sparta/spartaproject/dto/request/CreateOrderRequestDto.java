package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.order.OrderMethod;
import com.sparta.spartaproject.domain.order.OrderStatus;
import com.sparta.spartaproject.domain.order.PaymentMethod;

import java.util.UUID;

public record CreateOrderRequestDto(
        // todo 음식점 추후 변경
        UUID store_id,
        // todo 음식 추가
        // List<Food> foods = new ArrayList<>();
        int total_price,
        OrderStatus status,
        OrderMethod orderMethod,
        PaymentMethod payMethod,
        String request,
        String address
) {

}

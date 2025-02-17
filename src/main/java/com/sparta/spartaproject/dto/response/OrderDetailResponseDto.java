package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.order.OrderMethod;
import com.sparta.spartaproject.domain.order.OrderStatus;
import com.sparta.spartaproject.domain.order.PaymentMethod;

import java.util.UUID;

public record OrderDetailResponseDto(
        UUID orderId,
        OrderStatus status,
        int total_price,
        String storeName,
        OrderMethod orderMethod,
        PaymentMethod payMethod,
        String address,
        String request
        // todo foodResponseDto 추가하기
        // 음식명, 사이즈, 가격, 옵션 -> 음식 리스트
) {

}

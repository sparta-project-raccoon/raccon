package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.order.OrderStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateOrderStatusRequestDto {
    private UUID orderId;

    private OrderStatus orderStatus;
}

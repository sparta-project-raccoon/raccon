package com.sparta.spartaproject.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sparta.spartaproject.domain.order.OrderStatus;
import com.sparta.spartaproject.domain.order.OrderStatusDeserializer;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderStatusRequestDto {
    private UUID orderId;

    @JsonDeserialize(using = OrderStatusDeserializer.class)
    private OrderStatus orderStatus;
}

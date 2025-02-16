package com.sparta.spartaproject.domain.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderStatusRequestDto {
    private UUID orderId;

    @JsonDeserialize(using = OrderStatusDeserializer.class)
    private OrderStatus orderStatus;
}

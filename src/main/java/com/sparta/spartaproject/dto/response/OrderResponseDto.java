package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderResponseDto {
    private UUID orderId;
    private OrderStatus orderStatus;
    private int totalPrice;
    private String storeName;

//    private OrderMethod orderMethod;
//    private PaymentMethod paymentMethod;
//    private String address;
//    private String request;
}

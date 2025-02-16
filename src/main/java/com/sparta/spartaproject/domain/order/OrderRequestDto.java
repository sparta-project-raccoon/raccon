package com.sparta.spartaproject.domain.order;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderRequestDto {
    // todo 음식점 추후 변경
    private UUID store_id;

    // todo 음식 추가
//    List<Food> foods = new ArrayList<>();
    int totalPrice;

    private OrderStatus status;
    private OrderMethod orderMethod;
    private String request;
    private String address;
}

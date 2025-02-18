package com.sparta.spartaproject.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    WAIT("주문 접수 중입니다..."),
    ACCEPT("주문 접수 완료. 맛있게 조리중입니다."),
    FINISH("음식 준비 완료."),
    REFUSE("주문이 거절되었습니다."),
    CANCEL("주문이 취소되었습니다."),
    PENDING("주문 취소 가능 시간을 초과하셨습니다.");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
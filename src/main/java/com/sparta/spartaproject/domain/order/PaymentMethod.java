package com.sparta.spartaproject.domain.order;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("현금"),
    CARD("카드 결제"),
    STORE_PAYMENT("매장 결제");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}

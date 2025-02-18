package com.sparta.spartaproject.domain.order;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CARD("카드 결제");

    private String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}

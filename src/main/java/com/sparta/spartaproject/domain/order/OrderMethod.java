package com.sparta.spartaproject.domain.order;

import lombok.Getter;

@Getter
public enum OrderMethod {
    ONLINE("온라인 주문"),
    OFFLINE("매장 현장 주문");

    private final String description;

    OrderMethod(String description) {
        this.description = description;
    }
}
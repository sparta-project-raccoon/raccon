package com.sparta.spartaproject.domain.order;

public enum OrderMethod {
    ONLINE("온라인 주문"),
    OFFLINE("매장 현장 주문");

    private String description;

    OrderMethod(String description) {
        this.description = description;
    }
}

package com.sparta.spartaproject.domain.food;

import lombok.Getter;

@Getter
public enum Status {
    SALE("판매중"),
    SOLD_OUT("품절"),
    PREPARING("준비중");

    private final String description;

    Status(String description) { this.description = description; }
}
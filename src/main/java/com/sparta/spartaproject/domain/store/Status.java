package com.sparta.spartaproject.domain.store;

import lombok.Getter;

@Getter
public enum Status {
    BEFORE_OPEN("영업 전"),
    OPEN("영업 중"),
    CLOSED("영업 종료"),
    BREAK_TIME("브레이크 타임");

    private final String description;

    Status(String description) {
        this.description = description;
    }
}
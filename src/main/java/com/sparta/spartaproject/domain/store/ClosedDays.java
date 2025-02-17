package com.sparta.spartaproject.domain.store;

import lombok.Getter;

@Getter
public enum ClosedDays {
    MONDAY("월요일"),
    TUESDAY("화요일"),
    WEDNESDAY("수요일"),
    THURSDAY("목요일"),
    FRIDAY("금요일"),
    SATURDAY("토요일"),
    SUNDAY("일요일"),
    AllDay("연중무휴");

    private final String description;

    ClosedDays(String description) {
        this.description = description;
    }
}
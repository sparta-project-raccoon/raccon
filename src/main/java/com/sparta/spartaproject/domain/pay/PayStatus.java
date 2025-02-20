package com.sparta.spartaproject.domain.pay;

public enum PayStatus {
    READY("결제 전"),
    COMPLETED("결제 완료"),
    CANCELLED("결제 취소")
    ;

    private String description;

    PayStatus(String description) {
        this.description = description;
    }
}

package com.sparta.spartaproject.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    MASTER("마스터"),
    MANAGER("운영진"),
    OWNER("사장님"),
    CUSTOMER("고객");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
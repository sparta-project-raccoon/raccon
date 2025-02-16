package com.sparta.spartaproject.domain.image;

public enum Type {
    STORE("음식점"),
    REVIEW("리뷰");

    private final String description;

    Type(String description) {
        this.description = description;
    }
}

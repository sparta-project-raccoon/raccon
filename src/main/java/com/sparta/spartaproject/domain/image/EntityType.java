package com.sparta.spartaproject.domain.image;

import lombok.Getter;

@Getter
public enum EntityType {
    STORE("음식점"),
    FOOD("음식"),
    REVIEW("리뷰");

    private final String description;

    EntityType(String description) { this.description = description; }
}

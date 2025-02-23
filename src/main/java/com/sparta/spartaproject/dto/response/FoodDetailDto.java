package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.food.Status;

import java.util.UUID;

public record FoodDetailDto(
    UUID id,
    String name,
    Integer price,
    String description,
    String imagePath,
    Status status,
    Boolean isDisplayed
) {
}

package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.food.Status;

import java.util.UUID;

public record CreateFoodRequestDto(
    UUID storeId,
    String name,
    Integer price,
    String description,
    Status status
) {
}
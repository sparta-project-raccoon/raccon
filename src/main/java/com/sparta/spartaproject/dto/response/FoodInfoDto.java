package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.food.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record FoodInfoDto(
        UUID id,
        String name,
        Integer price,
        String description,
        String imagePath,
        Status status
) {
}

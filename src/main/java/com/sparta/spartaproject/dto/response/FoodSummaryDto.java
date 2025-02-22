package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record FoodSummaryDto(
    UUID id,
    String name,
    Integer price
) {
}
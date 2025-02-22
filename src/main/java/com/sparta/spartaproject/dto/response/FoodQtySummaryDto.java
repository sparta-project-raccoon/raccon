package com.sparta.spartaproject.dto.response;

public record FoodQtySummaryDto(
    Integer qty,
    FoodSummaryDto food
) {
}
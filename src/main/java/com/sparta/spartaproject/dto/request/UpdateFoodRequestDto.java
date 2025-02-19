package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.food.Status;

public record UpdateFoodRequestDto (
        String name,
        Integer price,
        String description,
        Status status
){
}

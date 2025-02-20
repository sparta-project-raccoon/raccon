package com.sparta.spartaproject.dto.response;

import java.util.List;

public record FoodDto(
    List<FoodDetailDto> foods,
    Integer currentPage,
    Integer totalPages
) {
}

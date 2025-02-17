package com.sparta.spartaproject.dto.response;

import java.util.List;

public record StoreByCategoryDto(
    List<OnlyStoreDto> stores,
    Integer currentPage,
    Integer totalPages,
    Integer totalElements
) {
}
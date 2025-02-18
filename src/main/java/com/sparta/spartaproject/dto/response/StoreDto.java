package com.sparta.spartaproject.dto.response;

import java.util.List;

public record StoreDto(
    List<StoreDetailDto> stores,
    Integer currentPage,
    Integer totalPages,
    Integer totalElements
) {
}
package com.sparta.spartaproject.dto.response;

import java.util.List;

public record PayHistoryDto(
    List<OnlyPayHistoryDto> onlyPayHistoryDtoList,
    Integer currentPage,
    Integer totalPages,
    Integer totalElements
) {
}

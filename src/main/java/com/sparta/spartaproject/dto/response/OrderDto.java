package com.sparta.spartaproject.dto.response;

import java.util.List;

public record OrderDto(
        List<OnlyOrderDto> onlyOrderDtoList,
        Integer currentPage,
        Integer totalPages,
        Integer totalElements
) {
}

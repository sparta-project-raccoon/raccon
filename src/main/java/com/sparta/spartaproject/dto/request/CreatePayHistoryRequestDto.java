package com.sparta.spartaproject.dto.request;

import java.util.UUID;

public record CreatePayHistoryRequestDto(
    UUID orderId
) {
}
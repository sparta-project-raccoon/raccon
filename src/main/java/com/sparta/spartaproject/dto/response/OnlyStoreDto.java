package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.store.ClosedDays;
import com.sparta.spartaproject.domain.store.Status;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record OnlyStoreDto(
    UUID id,
    String name,
    String address,
    Status status,
    String tel,
    String description,
    LocalTime openTime,
    LocalTime closeTime,
    ClosedDays closedDays,
    LocalDateTime createdAt,
    Long createdBy,
    LocalDateTime updatedAt,
    Long updatedBy
) {
}
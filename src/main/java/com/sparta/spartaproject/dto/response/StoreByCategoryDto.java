package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.store.ClosedDays;
import com.sparta.spartaproject.domain.store.Status;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record StoreByCategoryDto(
    CategoryDto category,
    List<String> imageUrls,
    double ratings,
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
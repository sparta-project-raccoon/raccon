package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.store.ClosedDays;
import com.sparta.spartaproject.domain.store.Status;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record UpdateStoreRequestDto(
    List<UUID> categoriesId,
    String name,
    String address,
    Status status,
    String tel,
    String description,
    LocalTime openTime,
    LocalTime closeTime,
    ClosedDays closedDays
) {
}
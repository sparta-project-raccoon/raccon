package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.store.ClosedDays;
import com.sparta.spartaproject.domain.store.Status;

import java.time.LocalTime;
import java.util.UUID;

public record CreateStoreRequestDto(
//        UUID categoryId,
        String name,
        String address,
        Status status,
        String tel,
        String description,
        LocalTime openTime,
        LocalTime closeTime,
        ClosedDays closedDays
){
}

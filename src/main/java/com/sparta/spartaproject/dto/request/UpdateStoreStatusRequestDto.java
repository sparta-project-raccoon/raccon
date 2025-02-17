package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.store.Status;

public record UpdateStoreStatusRequestDto(
    Status status
) {
}
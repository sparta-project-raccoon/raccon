package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record CategoryDto(
    UUID id,
    String name
) {
}
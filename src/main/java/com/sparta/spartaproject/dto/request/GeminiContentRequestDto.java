package com.sparta.spartaproject.dto.request;

import java.util.List;

public record GeminiContentRequestDto(
    List<GeminiPartsRequestDto> parts
) {
}
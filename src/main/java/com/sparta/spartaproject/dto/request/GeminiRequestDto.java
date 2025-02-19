package com.sparta.spartaproject.dto.request;

import java.util.List;

public record GeminiRequestDto(
    List<GeminiContentRequestDto> contents
) {
}
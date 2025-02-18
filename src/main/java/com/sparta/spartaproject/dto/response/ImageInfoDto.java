package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record ImageInfoDto(
        UUID imageId,   // 이미지 ID
        String path    // 이미지 저장 경로
) {
}

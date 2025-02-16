package com.sparta.spartaproject.dto.request;

import com.sparta.spartaproject.domain.image.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record SaveImageRequestDto(
        @NotBlank Type type, // "store" 또는 "review"
        @NotNull UUID id,  // storeId 또는 reviewId
        @NotNull List<MultipartFile> images // 업로드할 이미지 리스트
) {
}

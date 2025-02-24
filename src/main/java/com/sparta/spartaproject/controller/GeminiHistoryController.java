package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.common.pageable.PageableConfig;
import com.sparta.spartaproject.domain.gemini.GeminiHistoryService;
import com.sparta.spartaproject.dto.request.CreateGeminiHistoryRequestDto;
import com.sparta.spartaproject.dto.request.UpdateGeminiHistoryRequestDto;
import com.sparta.spartaproject.dto.response.GeminiHistoryResponseDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gemini")
public class GeminiHistoryController {
    private final GeminiHistoryService geminiHistoryService;
    private final PageableConfig pageableConfig;

    @Description(
        "Gemini History 전체 조회"
    )
    @GetMapping("/histories")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<List<GeminiHistoryResponseDto>> getGeminiHistories(
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(geminiHistoryService.getGeminiHistories(customPageable));
    }

    @Description(
        "Gemini History 상세 조회"
    )
    @GetMapping("/histories/{id}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<GeminiHistoryResponseDto> getGeminiHistory(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(geminiHistoryService.getGeminiHistory(id));
    }

    @Description(
        "Gemini History 생성"
    )
    @PostMapping("/histories")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> createGeminiHistory(@RequestBody CreateGeminiHistoryRequestDto request) {
        geminiHistoryService.createGeminiHistory(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "Gemini History 수정"
    )
    @PatchMapping("/histories/{id}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateGeminiHistory(@PathVariable("id") UUID id, @RequestBody UpdateGeminiHistoryRequestDto update) {
        geminiHistoryService.updateGeminiHistory(id, update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "Gemini History 삭제"
    )
    @DeleteMapping("/histories/{id}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> deleteGeminiHistory(@PathVariable("id") UUID id) {
        geminiHistoryService.deleteGeminiHistory(id);
        return ResponseEntity.ok().build();
    }
}
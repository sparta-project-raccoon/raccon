package com.sparta.spartaproject.domain.gemini;

import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateGeminiHistoryRequestDto;
import com.sparta.spartaproject.dto.request.UpdateGeminiHistoryRequestDto;
import com.sparta.spartaproject.dto.response.GeminiHistoryResponseDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.GeminiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiHistoryService {
    private final GeminiHistoryRepository geminiHistoryRepository;
    private final GeminiMapper geminiMapper;
    private final CircularService circularService;


    @Transactional(readOnly = true)
    public List<GeminiHistoryResponseDto> getGeminiHistories(Pageable customPageable) {
        return geminiHistoryRepository.findAllBySort(customPageable).stream().map(
            geminiMapper::toGeminiHistoryResponseDto
        ).toList();
    }

    @Transactional(readOnly = true)
    public GeminiHistoryResponseDto getGeminiHistory(@PathVariable UUID id) {
        GeminiHistory geminiHistory = geminiHistoryRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.GEMINI_HISTORY_NOT_FOUND));

        return geminiMapper.toGeminiHistoryResponseDto(geminiHistory);
    }

    @Transactional
    public void createGeminiHistory(CreateGeminiHistoryRequestDto request) {
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreById(request.storeId());

        GeminiHistory newGeminiHistory = geminiMapper.toGeminiHistory(request, user, store);
        geminiHistoryRepository.save(newGeminiHistory);
    }

    @Transactional
    public void updateGeminiHistory(UUID id, UpdateGeminiHistoryRequestDto update) {
        GeminiHistory geminiHistory = geminiHistoryRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.GEMINI_HISTORY_NOT_FOUND));

        Store store = circularService.getStoreService().getStoreById(update.storeId());

        geminiHistory.update(update, store);
    }

    public void deleteGeminiHistory(UUID id) {
        GeminiHistory geminiHistory = geminiHistoryRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.GEMINI_HISTORY_NOT_FOUND));

        geminiHistoryRepository.delete(geminiHistory);

        log.info("Gemini History: {}, 삭제", id);
    }
}
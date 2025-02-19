package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.gemini.GeminiHistory;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateGeminiHistoryRequestDto;
import com.sparta.spartaproject.dto.response.GeminiHistoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GeminiMapper {
    @Mapping(target = "userId", source = "geminiHistory.user.id")
    @Mapping(target = "storeId", source = "geminiHistory.store.id")
    GeminiHistoryResponseDto toGeminiHistoryResponseDto(GeminiHistory geminiHistory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "store", source = "store")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GeminiHistory toGeminiHistory(CreateGeminiHistoryRequestDto source, User user, Store store);
}
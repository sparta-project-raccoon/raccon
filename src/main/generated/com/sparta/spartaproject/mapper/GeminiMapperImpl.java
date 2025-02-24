package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.gemini.GeminiHistory;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateGeminiHistoryRequestDto;
import com.sparta.spartaproject.dto.response.GeminiHistoryResponseDto;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-24T08:47:52+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class GeminiMapperImpl implements GeminiMapper {

    @Override
    public GeminiHistoryResponseDto toGeminiHistoryResponseDto(GeminiHistory geminiHistory) {
        if ( geminiHistory == null ) {
            return null;
        }

        Long userId = null;
        UUID storeId = null;
        UUID id = null;
        String requestText = null;
        String responseText = null;
        Integer statusCode = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        userId = geminiHistoryUserId( geminiHistory );
        storeId = geminiHistoryStoreId( geminiHistory );
        id = geminiHistory.getId();
        requestText = geminiHistory.getRequestText();
        responseText = geminiHistory.getResponseText();
        statusCode = geminiHistory.getStatusCode();
        createdAt = geminiHistory.getCreatedAt();
        updatedAt = geminiHistory.getUpdatedAt();

        GeminiHistoryResponseDto geminiHistoryResponseDto = new GeminiHistoryResponseDto( id, userId, storeId, requestText, responseText, statusCode, createdAt, updatedAt );

        return geminiHistoryResponseDto;
    }

    @Override
    public GeminiHistory toGeminiHistory(CreateGeminiHistoryRequestDto source, User user, Store store) {
        if ( source == null && user == null && store == null ) {
            return null;
        }

        GeminiHistory.GeminiHistoryBuilder<?, ?> geminiHistory = GeminiHistory.builder();

        if ( source != null ) {
            geminiHistory.requestText( source.requestText() );
            geminiHistory.responseText( source.responseText() );
            geminiHistory.statusCode( source.statusCode() );
        }
        geminiHistory.user( user );
        geminiHistory.store( store );

        return geminiHistory.build();
    }

    private Long geminiHistoryUserId(GeminiHistory geminiHistory) {
        if ( geminiHistory == null ) {
            return null;
        }
        User user = geminiHistory.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID geminiHistoryStoreId(GeminiHistory geminiHistory) {
        if ( geminiHistory == null ) {
            return null;
        }
        Store store = geminiHistory.getStore();
        if ( store == null ) {
            return null;
        }
        UUID id = store.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}

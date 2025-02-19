package com.sparta.spartaproject.domain.gemini;

import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateGeminiHistoryRequestDto;
import com.sparta.spartaproject.dto.request.GeminiContentRequestDto;
import com.sparta.spartaproject.dto.request.GeminiPartsRequestDto;
import com.sparta.spartaproject.dto.request.GeminiRequestDto;
import com.sparta.spartaproject.mapper.GeminiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {
    private final CircularService circularService;
    private final GeminiHistoryRepository geminiHistoryRepository;
    private final GeminiMapper geminiMapper;

    @Value("${gemini.api.url}")
    private String geminiUrl;

    public String requestGemini(UUID storeId, String question) {
        RestTemplate restTemplate = new RestTemplate();

        String requestText = question + "에 대한 상품 설명을 50자 이내로 작성해줘.";
        List<GeminiPartsRequestDto> part = Collections.singletonList(new GeminiPartsRequestDto(requestText));
        List<GeminiContentRequestDto> contents = Collections.singletonList(new GeminiContentRequestDto(part));
        GeminiRequestDto geminiRequestDto = new GeminiRequestDto(contents);

        ResponseEntity<Map> response = restTemplate.postForEntity(geminiUrl, geminiRequestDto, Map.class);

        int statusCode = response.getStatusCode().value();
        log.info("Gemini API response: " + statusCode);

        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreById(storeId);

        String responseText = parseGeminiResponse(response);

        GeminiHistory newGeminiHistory = geminiMapper.toGeminiHistory(
            new CreateGeminiHistoryRequestDto(store.getId(), question, responseText, statusCode),
            user,
            store
        );

        geminiHistoryRepository.save(newGeminiHistory);

        return responseText;
    }

    public String parseGeminiResponse(ResponseEntity<Map> response) {
        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null) {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

            if (!candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);

                Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

                if (!parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                } else {
                    log.error("Gemini Parts 빈 값 추출");
                }
            } else {
                log.error("Gemini candidate 빈 값 추출");
            }
        } else {
            log.error("Gemini response body 빈 값 추출");
        }

        return null;
    }
}
package com.sparta.spartaproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spartaproject.common.pageable.PageableConfig;
import com.sparta.spartaproject.domain.review.ReviewService;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.request.UpdateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final PageableConfig pageableConfig;

    @Description(
        "리뷰 전체 조회"
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Page<ReviewDto>> getReviews(
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(reviewService.getReviews(customPageable));
    }

    @Description(
        "내가 작성한 리뷰 전체 조회"
    )
    @GetMapping("/my")
    public ResponseEntity<Page<ReviewDto>> getMyReviews(
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(reviewService.getMyReviews(customPageable));
    }

    @Description(
        "리뷰 상세 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @Description(
        "리뷰 등록"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createReview(
        @RequestPart("request") String requestJson,
        @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList
    ) throws JsonProcessingException {
        CreateReviewRequestDto request = new ObjectMapper().readValue(requestJson, CreateReviewRequestDto.class);
        reviewService.createReview(request, imageList);
        return ResponseEntity.ok().build();
    }

    @Description(
        "리뷰 수정"
    )
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateReview(
        @PathVariable("id") UUID id,
        @RequestPart("request") String updateJson,
        @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList
    ) throws JsonProcessingException {
        UpdateReviewRequestDto update = new ObjectMapper().readValue(updateJson, UpdateReviewRequestDto.class);
        reviewService.updateReview(id, update, imageList);
        return ResponseEntity.ok().build();
    }

    @Description(
        "리뷰 삭제하기"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "가게 리뷰 전체 조회"
    )
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<Page<ReviewDto>> getReviewsForStore(
        @PathVariable("storeId") UUID storeId,
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(reviewService.getReviewsForStore(storeId, customPageable));
    }

}
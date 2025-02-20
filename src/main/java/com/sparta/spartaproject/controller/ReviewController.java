package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.review.ReviewService;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.request.UpdateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Description(
        "리뷰 전체 조회"
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<List<ReviewDto>> getReviews(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(reviewService.getReviews(page, sortDirection));
    }

    @Description(
        "내가 작성한 리뷰 전체 조회"
    )
    @GetMapping("/my")
    public ResponseEntity<List<ReviewDto>> getMyReviews(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(reviewService.getMyReviews(page, sortDirection));
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
        @RequestPart("request") CreateReviewRequestDto request,
        @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList
    ) {
        reviewService.createReview(request, imageList);
        return ResponseEntity.ok().build();
    }

    @Description(
        "리뷰 수정"
    )
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateReview(
        @PathVariable("id") UUID id,
        @RequestPart("request") UpdateReviewRequestDto update,
        @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList
    ) {
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
    public ResponseEntity<List<ReviewDto>> getReviewsForStore(
        @PathVariable("storeId") UUID storeId,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        return ResponseEntity.ok(reviewService.getReviewsForStore(storeId, page, sortDirection));
    }

}
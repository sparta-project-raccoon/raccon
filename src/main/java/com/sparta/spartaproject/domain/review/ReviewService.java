package com.sparta.spartaproject.domain.review;

import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.request.UpdateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ReviewMapper reviewMapper;

    private Integer size = 10;

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviews(int page) {
        User user = userService.loginUser();

        Pageable pageable = PageRequest.of(page - 1, size);
        return reviewRepository.findAllByUserIdAndIsDeletedIsFalse(pageable, user.getId()).stream().map(
            reviewMapper::toReviewDto
        ).toList();
    }

    @Transactional(readOnly = true)
    public ReviewDto getReview(UUID id) {
        User user = userService.loginUser();
        Review review = getReviewByIdAndIsDeletedIsFalse(id);

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다.");
        }

        return reviewMapper.toReviewDto(review);
    }

    @Transactional
    public void createReview(CreateReviewRequestDto request) {
        User user = userService.loginUser();
        // Store store = storeService.findByIdAndIsDeletedIsFalse(request.storeId).orElseThrow();
        // Order order = orderService.findByIdAndIsDeletedIsFalse(request.orderId).orElseThrow();
        Review newReview = reviewMapper.toReview(request, user);

        reviewRepository.save(newReview);
    }

    @Transactional
    public void updateReview(UUID id, UpdateReviewRequestDto update) {
        User user = userService.loginUser();
        Review review = getReviewByIdAndIsDeletedIsFalse(id);

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다.");
        }

        review.update(update);
        log.info("리뷰: {}, 수정 완료", id);
    }

    @Transactional
    public void deleteReview(UUID id) {
        User user = userService.loginUser();
        Review review = getReviewByIdAndIsDeletedIsFalse(id);

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다.");
        }

        review.delete();
        log.info("리뷰: {}, 삭제 완료", id);
    }

    @Transactional
    public List<ReviewDto> getReviewsForStore(UUID storeId, int page) {
        // Store store = storeService.findByIdAndIsDeletedIsFalse(request.storeId).orElseThrow();

        Pageable pageable = PageRequest.of(page - 1, size);

        return reviewRepository.findAllByStoreIdAndIsDeletedIsFalse(pageable, storeId).stream().map(
            reviewMapper::toReviewDto
        ).toList();
    }


    public Review getReviewByIdAndIsDeletedIsFalse(UUID id) {
        return reviewRepository.findByIdAndIsDeletedIsFalse(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
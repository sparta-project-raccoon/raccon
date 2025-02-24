package com.sparta.spartaproject.domain.review;

import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.image.EntityType;
import com.sparta.spartaproject.domain.image.ImageService;
import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.request.UpdateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final CircularService circularService;
    private final ReviewRepository reviewRepository;
    private final ImageService imageService;


    @Transactional(readOnly = true)
    public Page<ReviewDto> getReviews(Pageable customPageable) {
        Page<Review> reviews = reviewRepository.findAllByIsDeletedIsFalse(customPageable);

        List<ReviewDto> dtos = reviews.stream().map(
            review -> {
                List<String> imageUrlList = imageService.getImageUrlByEntity(review.getId(), EntityType.REVIEW);
                log.info("리뷰 이미지 url - {}", imageUrlList);
                return reviewMapper.toReviewDtoWithImages(review, imageUrlList);
            }
        ).toList();

        return new PageImpl<>(dtos, customPageable, reviews.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> getMyReviews(Pageable customPageable) {
        User user = circularService.getUserService().loginUser();

        Page<Review> reviews = reviewRepository.findAllByUserIdAndIsDeletedIsFalse(customPageable, user.getId());

        List<ReviewDto> dtos = reviews.stream().map(
            review -> {
                List<String> imageUrlList = imageService.getImageUrlByEntity(review.getId(), EntityType.REVIEW);
                log.info("리뷰 이미지 개수 - {}", imageUrlList.size());
                return reviewMapper.toReviewDtoWithImages(review, imageUrlList);
            }
        ).toList();

        return new PageImpl<>(dtos, customPageable, reviews.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ReviewDto getReview(UUID id) {
        User user = circularService.getUserService().loginUser();
        Review review = getReviewByIdAndIsDeletedIsFalse(id);

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다.");
        }

        List<String> imageUrlList = imageService.getImageUrlByEntity(review.getId(), EntityType.REVIEW);
        imageUrlList.forEach(image -> log.info("리뷰의 이미지 url : {}", image));
        return reviewMapper.toReviewDtoWithImages(review, imageUrlList);
    }

    @Transactional
    public void createReview(CreateReviewRequestDto request, List<MultipartFile> imageList) {
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreByIdAndIsDeletedIsFalse(request.storeId());

        if (Objects.equals(store.getOwner().getId(), user.getId())) {
            throw new BusinessException(ErrorCode.STORE_OWNER_CANNOT_REVIEW_OWN_STORE);
        }

        // 주문 id + 로그인 id -> 주문 id 로 불러오기 - 마스터, 매니저도 쓸 수 있게
        Order order = circularService.getOrderService().getOrderById(request.orderId());

        if (reviewRepository.existsByStoreIdAndOrderIdAndIsDeletedIsFalse(store.getId(), order.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_WRITE_REVIEW);
        }

        Review newReview = reviewMapper.toReview(request, store, order, user);
        reviewRepository.save(newReview);

        if (imageList != null) {
            imageList.forEach(image -> {
                String path = imageService.uploadImage(newReview.getId(), EntityType.REVIEW, image);
                log.info("리뷰 이미지 저장 url : {}", path);
            });
        }
    }

    @Transactional
    public void updateReview(UUID id, UpdateReviewRequestDto update, List<MultipartFile> imageList) {
        User user = circularService.getUserService().loginUser();
        Review review = getReviewByIdAndIsDeletedIsFalse(id);

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다.");
        }

        imageService.deleteAllImagesByEntity(id, EntityType.REVIEW);

        if (imageList != null) {
            imageList.forEach(image -> {
                String url = imageService.uploadImage(id, EntityType.REVIEW, image);
                log.info("새로 생성된 URL : {}", url);
            });
        }

        review.update(update);
        log.info("리뷰: {}, 수정 완료", id);
    }

    @Transactional
    public void deleteReview(UUID id) {
        User user = circularService.getUserService().loginUser();
        Review review = getReviewByIdAndIsDeletedIsFalse(id);

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다.");
        }

        imageService.deleteAllImagesByEntity(id, EntityType.REVIEW);

        review.delete();
        log.info("리뷰: {}, 삭제 완료", id);
    }

    @Transactional
    public Page<ReviewDto> getReviewsForStore(UUID storeId, Pageable customPageable) {
        Store store = circularService.getStoreService().getStoreByIdAndIsDeletedIsFalse(storeId);

        Page<Review> reviews = reviewRepository.findAllByStoreIdAndIsDeletedIsFalse(customPageable, store.getId());

        List<ReviewDto> dtos = reviews.stream().map(
            review -> {
                List<String> imageUrlList = imageService.getImageUrlByEntity(review.getId(), EntityType.REVIEW);
                log.info("리뷰 이미지 url - {}", imageUrlList);
                return reviewMapper.toReviewDtoWithImages(review, imageUrlList);
            }
        ).toList();

        return new PageImpl<>(dtos, customPageable, reviews.getTotalElements());
    }


    public Review getReviewByIdAndIsDeletedIsFalse(UUID id) {
        return reviewRepository.findByIdAndIsDeletedIsFalse(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
    }

    public Long countByStoreId(UUID storeId) {
        return reviewRepository.countByStoreIdAndIsDeletedIsFalse(storeId);
    }
}
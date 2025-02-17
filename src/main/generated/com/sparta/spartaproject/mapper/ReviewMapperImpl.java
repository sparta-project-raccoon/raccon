package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.review.Review;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T22:20:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewDto toReviewDto(Review review) {
        if ( review == null ) {
            return null;
        }

        Long userId = null;
        UUID id = null;
        UUID storeId = null;
        UUID orderId = null;
        String content = null;
        Integer rating = null;

        userId = reviewUserId( review );
        id = review.getId();
        storeId = review.getStoreId();
        orderId = review.getOrderId();
        content = review.getContent();
        rating = review.getRating();

        ReviewDto reviewDto = new ReviewDto( id, userId, storeId, orderId, content, rating );

        return reviewDto;
    }

    @Override
    public Review toReview(CreateReviewRequestDto createReviewRequestDto, User user) {
        if ( createReviewRequestDto == null && user == null ) {
            return null;
        }

        Review.ReviewBuilder<?, ?> review = Review.builder();

        if ( createReviewRequestDto != null ) {
            review.storeId( createReviewRequestDto.storeId() );
            review.orderId( createReviewRequestDto.orderId() );
            review.content( createReviewRequestDto.content() );
            review.rating( createReviewRequestDto.rating() );
        }
        if ( user != null ) {
            review.user( user );
            review.createdAt( user.getCreatedAt() );
            review.updatedAt( user.getUpdatedAt() );
            review.isDeleted( user.getIsDeleted() );
            review.deletedAt( user.getDeletedAt() );
        }

        return review.build();
    }

    private Long reviewUserId(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}

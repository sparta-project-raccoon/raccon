package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.review.Review;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-19T21:05:29+0900",
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
        UUID storeId = null;
        UUID orderId = null;
        UUID id = null;
        String content = null;
        Integer rating = null;

        userId = reviewUserId( review );
        storeId = reviewStoreId( review );
        orderId = reviewOrderId( review );
        id = review.getId();
        content = review.getContent();
        rating = review.getRating();

        List<String> imageUrlList = null;

        ReviewDto reviewDto = new ReviewDto( id, userId, storeId, orderId, content, rating, imageUrlList );

        return reviewDto;
    }

    @Override
    public ReviewDto toReviewDtoWithImages(Review review, List<String> imageUrlList) {
        if ( review == null && imageUrlList == null ) {
            return null;
        }

        Long userId = null;
        UUID storeId = null;
        UUID orderId = null;
        UUID id = null;
        String content = null;
        Integer rating = null;
        if ( review != null ) {
            userId = reviewUserId( review );
            storeId = reviewStoreId( review );
            orderId = reviewOrderId( review );
            id = review.getId();
            content = review.getContent();
            rating = review.getRating();
        }
        List<String> imageUrlList1 = null;
        List<String> list = imageUrlList;
        if ( list != null ) {
            imageUrlList1 = new ArrayList<String>( list );
        }

        ReviewDto reviewDto = new ReviewDto( id, userId, storeId, orderId, content, rating, imageUrlList1 );

        return reviewDto;
    }

    @Override
    public Review toReview(CreateReviewRequestDto createReviewRequestDto, Store store, Order order, User user) {
        if ( createReviewRequestDto == null && store == null && order == null && user == null ) {
            return null;
        }

        Review.ReviewBuilder<?, ?> review = Review.builder();

        if ( createReviewRequestDto != null ) {
            review.content( createReviewRequestDto.content() );
            review.rating( createReviewRequestDto.rating() );
        }
        if ( store != null ) {
            review.store( store );
            review.createdBy( store.getCreatedBy() );
            review.updatedBy( store.getUpdatedBy() );
        }
        review.order( order );
        review.user( user );

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

    private UUID reviewStoreId(Review review) {
        if ( review == null ) {
            return null;
        }
        Store store = review.getStore();
        if ( store == null ) {
            return null;
        }
        UUID id = store.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID reviewOrderId(Review review) {
        if ( review == null ) {
            return null;
        }
        Order order = review.getOrder();
        if ( order == null ) {
            return null;
        }
        UUID id = order.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}

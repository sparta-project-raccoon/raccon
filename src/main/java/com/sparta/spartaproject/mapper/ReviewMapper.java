package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.review.Review;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "userId", source = "review.user.id")
    @Mapping(target = "storeId", source = "review.store.id")
    @Mapping(target = "orderId", source = "review.order.id")
    ReviewDto toReviewDto(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "store", source = "store")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Review toReview(CreateReviewRequestDto createReviewRequestDto, Store store, Order order, User user);
}
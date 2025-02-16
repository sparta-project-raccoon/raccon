package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.review.Review;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateReviewRequestDto;
import com.sparta.spartaproject.dto.response.ReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "userId", source = "review.user.id")
    ReviewDto toReviewDto(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Review toReview(CreateReviewRequestDto createReviewRequestDto, User user);
}
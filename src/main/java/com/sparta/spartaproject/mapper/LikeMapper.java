package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.like.Like;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateLikeRequestDto;
import com.sparta.spartaproject.dto.response.LikeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    @Mapping(target = "userId", source = "user.id")
    LikeDto toLikeDto(Like like);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Like toLike(CreateLikeRequestDto request, User user);
}
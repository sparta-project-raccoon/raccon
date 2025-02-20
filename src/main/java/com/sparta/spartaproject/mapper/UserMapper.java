package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.SignUpRequestDto;
import com.sparta.spartaproject.dto.response.FindUsernameDto;
import com.sparta.spartaproject.dto.response.UserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserInfoDto toUserInfoDto(User user);

    FindUsernameDto toFindUsernameDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "loginFailCount", ignore = true)
    @Mapping(target = "passwordChangedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(SignUpRequestDto source, String encodedPassword);
}
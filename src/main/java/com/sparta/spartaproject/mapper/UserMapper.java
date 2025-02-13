package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.response.FindUsernameDto;
import com.sparta.spartaproject.dto.response.UserInfoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserInfoDto toUserInfoDto(User user);

    FindUsernameDto toFindUsernameDto(User user);
}
package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.Status;

public record UserInfoDto(
    String email,
    String name,
    String phone,
    String address,
    Role role,
    Status status,
    Integer loginFailCount
) {
}
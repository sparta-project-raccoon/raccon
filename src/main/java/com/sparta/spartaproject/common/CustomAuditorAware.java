package com.sparta.spartaproject.common;

import com.sparta.spartaproject.domain.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 현재 로그인한 사용자 정보 추출
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return Optional.of(((User) principal).getUsername());
        }
        return Optional.empty();
    }
}
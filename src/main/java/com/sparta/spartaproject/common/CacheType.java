package com.sparta.spartaproject.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
    LOGIN_USER("loginUser", 180, 500),
    AUTHENTICATED_USER("authenticatedUser", 300, 500),

    STORE("store", 300, 100), // (5분 유지, 최대 100개 저장)
    STORES("stores", 300, 500); // (5분 유지, 최대 500개 저장)

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
}
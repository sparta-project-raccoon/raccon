package com.sparta.spartaproject.dto.response;

import com.sparta.spartaproject.domain.store.ClosedDays;

import java.time.LocalTime;


public record StoreDetailDto (
        String categoryName, // 카테고리 명 (ex : "분식")
        String name,
        String address,
        String statusDesc,  // 음식점 상태 (ex : Status.OPEN -> "영업 중")
        String tel,
        String description,
        LocalTime openTime,
        LocalTime closeTime,
        ClosedDays closedDays
){
}
package com.sparta.spartaproject.dto.response;

import java.util.UUID;

public record StoreSummaryDto (
        UUID storeId,
//        String categoryName, // 카테고리 명 (ex : "분식")
        String name,
        String statusDesc // 음식점 상태 (ex : Status.OPEN -> "영업 중")
){
}

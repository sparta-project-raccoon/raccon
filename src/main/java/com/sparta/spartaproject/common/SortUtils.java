package com.sparta.spartaproject.common;

import org.springframework.data.domain.Sort;

public class SortUtils {
    /**
     * 주어진 정렬 방향에 맞춰 Sort 객체를 반환합니다.
     *
     * @param sortDirection 정렬 방향 (asc 또는 desc)
     * @return 해당 필드에 대한 Sort 객체
     * "createdAt", 생성일자를 기준으로 정렬합니다.
     */
    public static Sort getSort(String sortDirection) {
        if ("desc".equalsIgnoreCase(sortDirection)) {
            return Sort.by(Sort.Order.desc("createdAt"));
        }
        return Sort.by(Sort.Order.asc("createdAt"));
    }

    /**
     * 주어진 정렬 방향에 맞춰 Sort 객체를 반환합니다.
     *
     * @param field         정렬할 필드명
     * @param sortDirection 정렬 방향 (asc 또는 desc)
     * @return 해당 필드에 대한 Sort 객체
     */
    public static Sort getSortByField(String field, String sortDirection) {
        if ("desc".equalsIgnoreCase(sortDirection)) {
            return Sort.by(Sort.Order.desc(field));
        }
        return Sort.by(Sort.Order.asc(field));
    }
}
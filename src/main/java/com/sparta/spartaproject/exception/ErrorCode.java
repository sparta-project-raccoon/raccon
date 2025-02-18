package com.sparta.spartaproject.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    //Common -> http 요청시 발생할만한 예외
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, " Invalid Input Value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, " Invalid Http Method"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, " Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access is Denied"),

    //Member Validation
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "중복 이메일입니다."),
    LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST, "비밀번호 혹은 아이디가 일치하지 않습니다."),
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

    // order
    ORDER_NOT_EXIST(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),
    CAN_NOT_CANCEL_ORDER(HttpStatus.BAD_REQUEST,"주문 취소 가능시간 5분이 지나 취소할 수 없습니다."),
    //room Validation


    //...등

    // 찜
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "찜 목록이 존재하지 않습니다."),

    // 리뷰
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다."),

    // 가게
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게 정보가 존재하지 않습니다."),
    STORE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "가게 정보에 대한 권한이 존재하지 않습니다."),

    // 카테고리
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리 정보가 존재하지 않습니다.");

    private final HttpStatus status; // http 상태코드
    private final String message;//에러메시지
}
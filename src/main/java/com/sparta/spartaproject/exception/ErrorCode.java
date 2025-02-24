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
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다."),
    PAGE_NOT_DOWN_ZERO(HttpStatus.BAD_REQUEST, "페이지 번호는 0보다 작을 수 없습니다."),
    NOT_MATCH_ENUM_TYPE(HttpStatus.BAD_REQUEST, "올바르지 않은 Enum 타입입니다."),


    //Member Validation
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "중복 이메일입니다."),
    LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST, "비밀번호 혹은 아이디가 일치하지 않습니다."),
    NOT_EXIST_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_STOPPED_USER(HttpStatus.BAD_REQUEST, "잠금 상태 회원이 아닙니다."),
    USER_NOT_AUTHENTICATED_OR_SUSPENDED(HttpStatus.UNAUTHORIZED, "인증이 완료되지 않은 사용자이거나 정지된 사용자입니다."),
    USERNAME_OR_EMAIL_ALREADY_IN_USE(HttpStatus.ALREADY_REPORTED, "이미 사용중인 아이디 또는 이메일 입니다."),
    SAME_PASSWORD_CHANGE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "같은 비밀번호로는 변경이 불가능합니다."),
    ALREADY_ROLE_MASTER(HttpStatus.BAD_REQUEST, "이미 마스터 권한입니다."),
    USER_INVALID_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    ALREADY_ROLE_MANAGER(HttpStatus.BAD_REQUEST, "이미 매니저 권한입니다."),
    ALREADY_ROLE_OWNER(HttpStatus.BAD_REQUEST, "이미 사장 권한입니다."),


    // order
    ORDER_NOT_EXIST(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),
    CAN_NOT_CANCEL_ORDER(HttpStatus.BAD_REQUEST, "주문 취소 가능시간 5분이 지나 취소할 수 없습니다."),
    ORDER_FORBIDDEN(HttpStatus.FORBIDDEN, "주문 상태 변경 권한이 존재하지 않습니다."),
    SELF_STORE_ORDER_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자신의 가게에는 주문할 수 없습니다."),


    // pay history
    PAY_HISTORY_NOT_EXIST(HttpStatus.NOT_FOUND, "결제 정보가 존재하지 않습니다."),
    //room Validation


    //...등

    // 찜
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "찜 목록이 존재하지 않습니다."),
    LIKE_FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다."),

    // 리뷰
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰가 존재하지 않습니다."),
    STORE_OWNER_CANNOT_REVIEW_OWN_STORE(HttpStatus.BAD_REQUEST, "가게 사장은 자신의 가게에 리뷰를 작성할 수 없습니다."),
    ALREADY_WRITE_REVIEW(HttpStatus.BAD_REQUEST, "이미 작성된 리뷰입니다."),

    // 가게
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게 정보가 존재하지 않습니다."),
    STORE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "가게 정보에 대한 권한이 존재하지 않습니다."),
    ALREADY_IS_TRUE(HttpStatus.BAD_REQUEST, "이미 허가된 가게입니다."),

    // 카테고리
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리 정보가 존재하지 않습니다."),
    CATEGORY_IS_USED(HttpStatus.BAD_REQUEST, "사용중인 카테고리이므로 삭제할 수 없습니다."),
    ALREADY_CATEGORY(HttpStatus.BAD_REQUEST, "이미 사용중인 카테고리 입니다."),

    // 음식
    FOOD_FORBIDDEN(HttpStatus.FORBIDDEN, "현재 로그인한 사용자와 업주가 일치하지 않습니다."),
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "음식 정보가 존재하지 않습니다."),
    FOOD_NOT_IN_STORE(HttpStatus.BAD_REQUEST, "가게에 등록되어 있지 않은 음식입니다."),

    // 인증
    MIN_RE_AUTHENTICATION_TIME_NOT_PASSED(HttpStatus.BAD_REQUEST, "3분 내 재인증 요청할 수 없습니다."),
    EMAIL_NOT_FOUND_IN_VERIFICATION_LIST(HttpStatus.NOT_FOUND, "인증 이메일 목록에 이메일이 존재하지 않습니다."),
    VERIFICATION_CODE_REQUEST_TIMEOUT(HttpStatus.BAD_REQUEST, "인증번호 요청 시간이 3분을 초과했습니다."),
    EXCEEDED_MAX_VERIFICATION_ATTEMPTS(HttpStatus.BAD_REQUEST, "인증 코드 불일치 5회 초과, 재인증 요청해주세요."),
    VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다."),

    // Gemini History
    GEMINI_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Gemini 내역이 존재하지 않습니다."),

    // 이미지
    IMAGE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패하였습니다.");


    private final HttpStatus status; // http 상태코드
    private final String message;//에러메시지
}
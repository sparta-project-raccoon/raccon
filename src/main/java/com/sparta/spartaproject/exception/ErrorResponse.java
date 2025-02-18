package com.sparta.spartaproject.exception;

import lombok.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message; //에러메시지
    private int status; // http상태코드

    //생성자는 private해되야 정적 팩토리 메소드가 의미가있다.
    private ErrorResponse(final ErrorCode errorCode) { //에러코드객체만 들어왔다면 그 객체를 이용해서 에러응답객체를 만들어준다.
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus().value();
    }

    private ErrorResponse(final ErrorCode errorCode, final List<FieldError> errors) { //에러코드객체와, 필드에러리스트가 들어왔을때 응답객체 생성
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus().value();
    }

    //위의 만들어둔 private 생성자를 통해,  정적 팩토리 메소드를 만들어 준다. (생성자 대신 생성을 담당할 메소드) , of라는 메소드를 오버로딩해서 생성
    public static ErrorResponse of(final ErrorCode errorCode) { // 에러코드만 받았을때
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(final ErrorCode errorCode, final List<FieldError> errors) { //에러코드와 필드에러 리스트를 받았을때
        return new ErrorResponse(errorCode, errors);
    }

    //에러코드와 BindingResult 객체가 들어왔을경우
    public static ErrorResponse of(final ErrorCode errorCode, final BindingResult bindingResult) {
        return new ErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString(); //들어온 값
        final List<ErrorResponse.FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode()); //e.getName() => method이름
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class FieldError { //필드에서 발생한 유효성검사 실패에 대한 정보를 저장할 클래스
        private String field; // 클래스명.유효성검사 실패한 변수명이 저장될 변수가 저장된다 (ex) member.name
        private String value; // 들어온 값
        private String reason; // 유효성 검사 조건

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        private static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) { //BindingResult는 검증오류가 발생할 경우 검증오류를 보관하는 객체(추가공부필요)
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors(); //bindingResult에 저장되는 springframeworkd에서 지원하는 fielderror 가져온다
            return fieldErrors.stream() //내가 만든 fielderror로 매핑
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}

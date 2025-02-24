package com.sparta.spartaproject.exception;

import com.sparta.spartaproject.domain.slack.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final SlackService slackService;

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AuthorizationDeniedException e) {
        log.warn("권한 에러 : {} ", e.getMessage());
        final ErrorCode code = ErrorCode.HANDLE_ACCESS_DENIED;
        final ErrorResponse response = ErrorResponse.of(code);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /*
    enum type이 일치하지 않아 binding 못할경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_MATCH_ENUM_TYPE);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /*
    지원하지 않은 Http Method방식으로 호출할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @ExceptionHandler(BusinessException.class) //비즈니스로직에서 던져지는,발생되는 예외들에 대한 처리 (validation오류들과 같은)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("handle 내부 로직 에러(BusinessException) : {} ", e.getMessage());
        final ErrorCode code = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(code);
        slackService.sendMessage(e, request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException e) {
        log.warn("handleConstraintViolationException : {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(ErrorCode.PAGE_NOT_DOWN_ZERO);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class) // 위의 예외들에서 걸러지지않은 나머지 예외들에 대한 처리
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleAllException : ", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}

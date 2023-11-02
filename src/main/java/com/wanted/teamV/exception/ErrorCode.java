package com.wanted.teamV.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_FOUND(HttpStatus.BAD_REQUEST, "요청사항을 찾지 못했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    INVALID_PAGE_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 페이지 요청입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다."),
    DUPLICATE_ACCOUNT(HttpStatus.BAD_REQUEST, "중복된 계정입니다."),
    EXPIRE_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    EMPTY_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "인증헤더가 비어있습니다."),
    FAIL_READ_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기 작업이 실패했습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(final HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
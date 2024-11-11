package com.example.schedules.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 잘못된 입력값 */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "잘못된 입력값입니다."),
    INVALID_INPUT_PARAM(HttpStatus.BAD_REQUEST, "INVALID_INPUT_PARAM", "잘못된 파라미터 값입니다."),

    /* 405 비밀 번호 확인 */
    INVALID_PASSWORD(HttpStatus.FORBIDDEN,"INVALID_PASSWORD","비밀 번호가 일치하지 않습니다."),

    /* 404 일정 찾을 수 없음*/
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE_NOT_FOUN","해당 일정을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUN","해당 작성자를 찾을 수 없습니다."),

    /* 500 내부 서버 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "내부 서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

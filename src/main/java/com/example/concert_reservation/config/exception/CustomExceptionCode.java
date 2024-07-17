package com.example.concert_reservation.config.exception;

import lombok.Getter;

@Getter
public enum CustomExceptionCode {

    USER_NOT_FOUND(404, "USER_NOT_FOUND", "해당 유저를 찾을 수 없습니다."),
    TOKEN_NOT_VALID(401, "TOKEN_NOT_VALID", "토큰이 만료되었습니다."),
    USER_CAN_NOT_BE_NULL(400, "USER_CAN_NOT_BE_NULL", "사용자는 null이 될 수 없습니다."),
    CHARGE_POINT_NOT_BE_UNDER_ZERO(400, "CHARGE_POINT_NOT_BE_UNDER_ZERO", "충전 포인트는 0이하가 될 수 없습니다.");

    //상태코드
    private final int status;
    //카테고리화한 오류코드
    private final String code;
    //예외 메세지
    private final String message;

    CustomExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

package com.example.concert_reservation.config.exception;

import lombok.Getter;

@Getter
public enum CustomExceptionCode {

    USER_NOT_FOUND(404, "USER_NOT_FOUND", "해당 유저를 찾을 수 없습니다."),
    USER_CAN_NOT_BE_NULL(400, "USER_CAN_NOT_BE_NULL", "사용자는 null이 될 수 없습니다."),
    TOKEN_NOT_FOUND(404, "TOKEN_NOT_FOUND", "토큰 정보가 없습니다."),
    TOKEN_NOT_VALID(401, "TOKEN_NOT_VALID", "토큰이 만료되었습니다."),
    CHARGE_POINT_NOT_BE_UNDER_ZERO(400, "CHARGE_POINT_NOT_BE_UNDER_ZERO", "충전 포인트는 0이하가 될 수 없습니다."),
    RESERVATION_NOT_FOUND(404,"RESERVATION_NOT_FOUND", "예약 정보가 없습니다."),
    RESERVATION_EXIST(403, "RESERVATION_IS_EXIST", "이미 예약된 정보가 있습니다."),
    RESERVATION_FAILED(500, "RESERVATION_FAILED", "예약이 실패하였습니다."),
    PAYMENT_TIME_EXPIRE(403, "PAYMENT_TIME_EXPIRE", "결제 시간이 만료되었습니다."),
    PAYMENT_DIFFERENT_USER(403, "PAYMENT_DIFFERENT_USER", "예약한 유저가 아닙니다."),
    POINT_NOT_ENOUGH(403, "POINT_NOT_ENOUGH", "유저의 포인트가 결제금액보다 적습니다."),
    USER_POINT_NOT_FOUND(404, "USER_POINT_NOT_FOUND", "유저의 포인트 정보가 없습니다."),
    SEAT_NOT_FOUND(404, "SEAT_NOT_FOUND", "좌석 정보가 없습니다."),
    NO_TOKEN_KEY(400, "NO_TOKEN_REQUEST", "요청 헤더에서 토큰키를 찾을 수 없습니다."),
    INVALID_TOKEN_KEY(400, "INVALID_TOKEN_KEY", "유효하지않은 토큰키 입니다."),
    INVALID_CONCERT_ID(400, "INVALID_CONCERT_ID", "유효하지않은 콘서트ID 입니다."),
    INVALID_SCHEDULE_ID(400, "INVALID_SCHEDULE_ID", "유효하지않은 스케쥴ID 입니다."),
    INVALID_SEAT_ID(400, "INVALID_SEAT_ID", "유효하지않은 좌석ID 입니다."),
    INVALID_RESERVATION_ID(400, "INVALID_RESERVATION_ID", "유효하지않은 예약ID 입니다.")

    ;

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

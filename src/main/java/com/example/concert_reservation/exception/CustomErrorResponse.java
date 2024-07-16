package com.example.concert_reservation.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class CustomErrorResponse {

    private int status;
    private String code;
    private String message;
    // 상세 에러 메시지
    private List<FieldError> errors;
    // 에러 이유
    private String reason;

    @Builder
    public CustomErrorResponse(int status, String code, String message, List<FieldError> errors, String reason) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.reason = reason;
    }
}

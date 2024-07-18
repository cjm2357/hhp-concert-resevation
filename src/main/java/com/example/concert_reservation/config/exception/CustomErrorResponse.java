package com.example.concert_reservation.config.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class CustomErrorResponse {

    private int status;
    private String code;
    private String message;

    @Builder
    public CustomErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

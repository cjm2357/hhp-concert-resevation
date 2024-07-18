package com.example.concert_reservation.config.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final CustomExceptionCode customExceptionCode;

    public CustomException(CustomExceptionCode customExceptionCode) {
        this.customExceptionCode = customExceptionCode;
    }
}

package com.example.concert_reservation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.concert_reservation.exception.CustomExceptionCode.USER_NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class RestApiGlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException ex) {
        log.error("Custom Exception Error", ex);

        CustomExceptionCode customExceptionCode = ex.getCustomExceptionCode();
        final CustomErrorResponse errorResponse =
                CustomErrorResponse.builder()
                        .status(customExceptionCode.getStatus())
                        .code(customExceptionCode.getCode())
                        .message(customExceptionCode.getMessage())
                        .reason(ex.getCause().toString())
                .build();


        new CustomException(USER_NOT_FOUND);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));

    }
}

package com.example.concert_reservation.presentation.controller;

import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.dto.TokenRequestDto;
import com.example.concert_reservation.dto.TokenResponseDto;
import com.example.concert_reservation.domain.entity.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
public class TokenController {

    private final TokenFacade tokenFacade;

    public TokenController(TokenFacade tokenFacade) {
        this.tokenFacade = tokenFacade;
    }

    // 토큰 발급 API
    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody TokenRequestDto dto) {
        if (dto.getUserId() == null)  {
            log.warn("no user id");
            throw new CustomException(CustomExceptionCode.USER_CAN_NOT_BE_NULL);
        }

        Token token = tokenFacade.createToken(dto.getUserId());
        TokenResponseDto responseDto = new TokenResponseDto(token);
        return ResponseEntity.ok(responseDto);

    }

    // 토큰 대기열 조회 API
    @GetMapping("/token/status")
    public ResponseEntity<?> readTokenStatus(@RequestHeader(value = "Authorization", required = false) UUID key) {
        if (key != null) {
            Token token = tokenFacade.getTokenStatus(key);
            TokenResponseDto responseDto = new TokenResponseDto(token);
            return ResponseEntity.ok(responseDto);
        } else {
            log.warn("no token key or invalid token");
            throw new CustomException(CustomExceptionCode.INVALID_TOKEN_KEY);
        }
    }
}

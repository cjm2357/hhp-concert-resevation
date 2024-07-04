package com.example.concert_reservation.controller;

import com.example.concert_reservation.dto.TokenRequestDto;
import com.example.concert_reservation.dto.TokenResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TokenController {


    // 토큰 발급 API
    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody TokenRequestDto dto) {
        if (dto.getUserId() != null) {
            TokenResponseDto responseDto = new TokenResponseDto();
            responseDto.setKey(UUID.randomUUID());
            responseDto.setOrder(100);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no user ID");
        }

    }

    // 토큰 대기열 조회 API
    @GetMapping("/token/status")
    public ResponseEntity<?> readTokenStatus(@RequestHeader(value = "Authorization", required = false) UUID key) {
        if (key != null) {
            TokenResponseDto responseDto = new TokenResponseDto();
            responseDto.setKey(key);
            responseDto.setOrder(10);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("invalid token");
        }
    }
}

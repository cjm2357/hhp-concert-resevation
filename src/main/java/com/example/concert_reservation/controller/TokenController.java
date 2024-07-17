package com.example.concert_reservation.controller;

import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.dto.TokenRequestDto;
import com.example.concert_reservation.dto.TokenResponseDto;
import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TokenController {

    private final TokenFacade tokenFacade;

    public TokenController(TokenFacade tokenFacade) {
        this.tokenFacade = tokenFacade;
    }

    // 토큰 발급 API
    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody TokenRequestDto dto) {
        if (dto.getUserId() == null)  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no user ID");
        }

        Token token = tokenFacade.getToken(dto.getUserId());
        TokenResponseDto responseDto = new TokenResponseDto(token);
        return ResponseEntity.ok(responseDto);



    }

    // 토큰 대기열 조회 API
    @GetMapping("/token/status")
    public ResponseEntity<?> readTokenStatus(@RequestHeader(value = "Authorization", required = false) UUID key) {
        if (key != null) {
            Token token = tokenFacade.getTokenStatusAndUpdate(key);
            TokenResponseDto responseDto = new TokenResponseDto(token);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("invalid token");
        }
    }
}

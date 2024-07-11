package com.example.concert_reservation.manager;

import com.example.concert_reservation.service.TokenService;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import java.util.UUID;


/**
 * 컨트롤러와 서비스 계층 사이?
 *
 * */
@Component
public class TokenManager {

    private final TokenService tokenService;

    public TokenManager(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void validateToken(UUID tokenKey) throws AuthenticationException {
        boolean hasEnroll = tokenService.checkTokenActivate(tokenKey);
        if (!hasEnroll) {
            throw new AuthenticationException("토큰이 만료되었습니다.");
        }

    }
}

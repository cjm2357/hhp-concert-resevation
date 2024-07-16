package com.example.concert_reservation.application;


import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class TokenFacade {

    private final TokenService tokenService;

    public TokenFacade(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Transactional
    public Token getToken(Integer userId) {
       return tokenService.getToken(userId);
    }

    @Transactional
    public Token getTokenStatusAndUpdate(UUID key) {
       return getTokenStatusAndUpdate(key);
    }

    public boolean checkTokenActivate(UUID tokenKey) {
        return tokenService.checkTokenActivate(tokenKey);
    }


    public void expireToken() {
        //ACTIVATE 토큰만 만료시간이 되면 만료시킨다.
        tokenService.expireToken();
    }
}

package com.example.concert_reservation.application;


import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.service.TokenService;
import com.example.concert_reservation.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
public class TokenFacade {

    private final TokenService tokenService;
    private final UserService userService;

    public TokenFacade(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Transactional
    public Token getToken(Integer userId) {
        User user = userService.getUser(userId);
        Token token = tokenService.getToken(userId);
        log.info("{} user get {} token", userId, token.getTokenKey());
       return token;
    }

    @Transactional
    public Token getTokenStatusAndUpdate(UUID key) {
       return tokenService.getTokenStatusAndUpdate(key);
    }

    public Token getTokenInfo(UUID tokenKey) { return tokenService.getTokenInfo(tokenKey);}

    public void expireToken() {
        //ACTIVATE 토큰만 만료시간이 되면 만료시킨다.
        tokenService.expireToken();
    }
}

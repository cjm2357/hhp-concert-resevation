package com.example.concert_reservation.application;


import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
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
    public Token createToken(Integer userId) {
        User user = userService.getUser(userId);
        Token token = tokenService.createToken(userId);
        log.info("{} user get {} token", userId, token.getTokenKey());
       return token;
    }

    public Token getTokenStatus(UUID key) {
        return tokenService.getTokenStatus(key);
    }

//    @Transactional
//    public Token getTokenStatusAndUpdate(UUID key) {
//       return tokenService.getTokenStatus(key);
//    }

//    public Token getTokenInfo(UUID tokenKey) { return tokenService.getTokenStatus(tokenKey);}

    public void expireToken(UUID tokenKey) {
        //ACTIVATE 토큰만 만료시간이 되면 만료시킨다.
        tokenService.expireToken(tokenKey);
    }

    public void activateTokens(Integer activateCount) {
        tokenService.activateTokens(activateCount);
    }
}

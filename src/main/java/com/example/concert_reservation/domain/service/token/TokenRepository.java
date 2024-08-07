package com.example.concert_reservation.domain.service.token;

import com.example.concert_reservation.domain.entity.Token;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface TokenRepository {

    Token save(Token token);
    Token findByTokenKey(UUID key);
    List<Token> findByStateOrderById(Token.TokenState state);

    void expireToken(LocalDateTime localDateTime);
    void expireToken(UUID tokenKey);

    void activateTokens(Integer activateCount);


    void updateStateToExpiredByUserId(Integer userId);

}

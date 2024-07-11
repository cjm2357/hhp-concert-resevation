package com.example.concert_reservation.service.repository;

import com.example.concert_reservation.entity.Token;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface TokenRepository {

    Token save(Token token);
    Token findFirstByStateOrderByIdDesc(Token.TokenState tokenState);
    Token findByTokenKey(UUID key);
    List<Token> findByStateOrderById(Token.TokenState state);

    void expireToken(LocalDateTime localDateTime);

}

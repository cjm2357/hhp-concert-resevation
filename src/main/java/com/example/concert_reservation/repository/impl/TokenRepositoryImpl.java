package com.example.concert_reservation.repository.impl;

import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.repository.TokenJpaRepository;
import com.example.concert_reservation.service.repository.TokenRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public class TokenRepositoryImpl implements TokenRepository{

    private final TokenJpaRepository tokenJpaRepository;

    public TokenRepositoryImpl(TokenJpaRepository tokenJpaRepository) {
        this.tokenJpaRepository = tokenJpaRepository;
    }

    public Token save(Token token) {
        return tokenJpaRepository.save(token);
    };


    public Token findFirstByStateOrderByIdDesc(Token.TokenState tokenState) {
        return tokenJpaRepository.findFirstByStateOrderByIdDesc(tokenState);
    }

    public Token findByTokenKey(UUID key) {
        return tokenJpaRepository.findByTokenKey(key);
    }

    public List<Token> findByStateOrderById(Token.TokenState state) {
        return tokenJpaRepository.findByStateOrderById(state);
    }

    public void expireToken(LocalDateTime localDateTime) {
        tokenJpaRepository.updateStateExpired(localDateTime);
    }

}

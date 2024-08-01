package com.example.concert_reservation.domain.service;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.service.repository.TokenRepository;
import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.repository.TokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenRedisRepository tokenRedisRepository;


    public Token createToken(Integer userId) {
        return tokenRedisRepository.createToken(userId);
    }

    public Token getTokenStatus(UUID key) {
        return tokenRedisRepository.getTokenStatus(key);
    }


    public void expireToken(UUID tokenKey) {
        //ACTIVATE 토큰만 만료시간이 되면 만료시킨다.
        tokenRedisRepository.expireToken(tokenKey);
    }

    public void activateTokens(Integer activateCount) {
        tokenRedisRepository.activateTokens(activateCount);
    }
}

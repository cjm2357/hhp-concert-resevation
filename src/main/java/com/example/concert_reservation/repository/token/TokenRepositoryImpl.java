package com.example.concert_reservation.repository.token;

import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.repository.token.TokenJpaRepository;
import com.example.concert_reservation.domain.service.token.TokenRepository;
import com.example.concert_reservation.repository.token.TokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository{

    private final TokenJpaRepository tokenJpaRepository;
    private final TokenRedisRepository tokenRedisRepository;

    public Token save(Token token) {
        return tokenRedisRepository.createToken(token);
    };

    public Token findByTokenKey(UUID key) {
        return tokenRedisRepository.getTokenStatus(key);
    }

    public List<Token> findByStateOrderById(Token.TokenState state) {
        return tokenJpaRepository.findByStateOrderById(state);
    }

    public void expireToken(LocalDateTime localDateTime) {
        tokenJpaRepository.updateStateExpired(localDateTime);
    }

    public void expireToken(UUID tokenKey) {tokenRedisRepository.expireToken(tokenKey);}

    public void updateStateToExpiredByUserId(Integer userId) {
        tokenJpaRepository.updateStateToExpiredByUserId(userId);
    }

    public void activateTokens(Integer activateCount) { tokenRedisRepository.activateTokens(activateCount);}

}

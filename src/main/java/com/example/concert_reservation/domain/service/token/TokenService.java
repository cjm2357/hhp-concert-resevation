package com.example.concert_reservation.domain.service.token;

import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;


    public Token createToken(User user) {
        Token token = Token.builder()
                .key(UUID.randomUUID())
                .user(user)
                .build();

        return tokenRepository.save(token);
    }

    public Token getTokenStatus(UUID key) {
        return tokenRepository.findByTokenKey(key);
    }


    public void expireToken(UUID tokenKey) {
        //ACTIVATE 토큰만 만료시간이 되면 만료시킨다.
        tokenRepository.expireToken(tokenKey);
    }

    public void activateTokens(Integer activateCount) {
        tokenRepository.activateTokens(activateCount);
    }
}

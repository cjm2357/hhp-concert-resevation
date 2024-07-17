package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    public TokenService (TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }


    @Transactional
    public Token getToken(Integer userId) {

        if (userId == null) {
            throw new IllegalArgumentException("유효하지않은 userId 입니다.");
        }

        User user = new User();
        user.setId(userId);

        //토큰생성 직후에는 Token의 Id를 모르기때문에 일단 WAITING 상태로 둔후
        //Save 후 대기번호 계산
        Token token = Token.builder()
                .user(user)
                .key(UUID.randomUUID())
                .createdTime(LocalDateTime.now())
                .state(Token.TokenState.WAITING)
                .expiredTime(LocalDateTime.now().plusMinutes(Token.EXPIRED_TIME_TEN_MIN))
                .build();

        token = tokenRepository.save(token);


        List<Token> activeTokens = tokenRepository.findByStateOrderById(Token.TokenState.ACTIVATE);


        int order = calcOrder(token, activeTokens);
        if (order == 0) {
            token.setState(Token.TokenState.ACTIVATE);
            token = tokenRepository.save(token);
        }
        token.setOrder(order);


        return token;

    }

    @Transactional
    public Token getTokenStatusAndUpdate(UUID key) {

        if (key == null) {
            throw new IllegalArgumentException("유효하지않은 토큰 key 입니다.");
        }

        Token token = tokenRepository.findByTokenKey(key);
        if (token.getState() == Token.TokenState.ACTIVATE) {
            token.setOrder(0);
            return token;
        }

        List<Token> activeTokens = tokenRepository.findByStateOrderById(Token.TokenState.ACTIVATE);
        int order = calcOrder(token, activeTokens);
        if (order == 0) {
            token.setState(Token.TokenState.ACTIVATE);
            tokenRepository.save(token);
        }
        token.setOrder(order);

        return token;
    }

    public boolean checkTokenActivate(UUID tokenKey) {
        Token token = tokenRepository.findByTokenKey(tokenKey);
        if (token == null) throw new RuntimeException("토큰 정보가 없습니다.");
        if (token.getState() == Token.TokenState.ACTIVATE) return true;
        return false;
    }

    public Token getTokenInfo(UUID tokenKey) {
        Token token = tokenRepository.findByTokenKey(tokenKey);
        if (token == null) throw new RuntimeException("토큰 정보가 없습니다.");
        return token;
    }

    public void expireToken() {
        //ACTIVATE 토큰만 만료시간이 되면 만료시킨다.
        tokenRepository.expireToken(LocalDateTime.now());
    }

    public void updateStateToExpiredByUserId(Integer userId) {
         tokenRepository.updateStateToExpiredByUserId(userId);
    }

    private Integer calcOrder(Token token, List<Token> activeTokens) {

        int activeCount = activeTokens.size();
        Token lastActivatedToken = null;
        if (activeCount > 0 ) lastActivatedToken = activeTokens.get(activeCount-1);

        int lastActivatedTokenId = 0;
        if (lastActivatedToken != null) lastActivatedTokenId = lastActivatedToken.getId();

        // 토큰의 id를 auto increment로 생성함으로서
        // 토큰ID - 마지막 활성화ID - (남은 Active 여유자리) 해서 0 이하일 경우 activate할 수 있는 조건
        int order = token.getId() - lastActivatedTokenId  - (Token.AVAILABLE_NUM - activeCount);
        if (order < 0) order = 0;
        return order;
    }
}

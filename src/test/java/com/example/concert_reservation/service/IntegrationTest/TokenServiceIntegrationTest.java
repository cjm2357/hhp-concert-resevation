package com.example.concert_reservation.service.IntegrationTest;


import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.TokenService;
import com.example.concert_reservation.service.repository.TokenRepository;
import com.example.concert_reservation.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TokenServiceIntegrationTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void init() {
        User user = new User();
        user.setId(1);
        user.setName("유저1");
        userRepository.save(user);

    }

    @Test
    void 토큰발급후_대기없을때_바로_active() {
        //given
        Integer userId = 1;

        //when
        Token token = tokenService.getToken(userId);

        //then
        assertEquals(userId, token.getUser().getId());
        assertEquals(0, token.getOrder());
    }


    @Test
    void 토큰조회() {
        //given
        Integer userId = 1;
        Token tokenInfo = tokenService.getToken(userId);
        
        //when
        Token token = tokenService.getTokenStatusAndUpdate(tokenInfo.getTokenKey());

        //then
        assertEquals(userId, tokenInfo.getUser().getId());
        assertEquals(token.getOrder(), 0);

    }


    @Test
    void 토큰50개이상_대기번호_발급 () {
        //given

        for (int i = 1; i< 61; i++) {

            User user = new User();
            user.setId(i+1);
            user.setName("유저" +(i+1));
            userRepository.save(user);
            tokenService.getToken(i);
        }

        int userId = 61;

        //when
        Token token = tokenService.getToken(userId);

        //then
        assertEquals(userId, token.getUser().getId());
        assertEquals(token.getOrder(), 61-50);
    }



    @Test
    void 토큰만료시키기_성공() {
        //given
        User user = new User();
        user.setId(2);
        user.setName("유저2" );
        user = userRepository.save(user);

        User user2 = new User();
        user2.setId(3);
        user2.setName("유저3" );
        user2 = userRepository.save(user2);

        Token token1 = new Token();
        token1.setTokenKey(UUID.randomUUID());
        token1.setState(Token.TokenState.EXPIRED);
        token1.setUser(user);
        token1.setCreatedTime(LocalDateTime.now().minusMinutes(13));
        token1.setExpiredTime(LocalDateTime.now().minusMinutes(3));
        token1 = tokenRepository.save(token1);

        Token token2 = new Token();
        token2.setTokenKey(UUID.randomUUID());
        token2.setState(Token.TokenState.WAITING);
        token2.setUser(user2);
        token2.setCreatedTime(LocalDateTime.now().minusMinutes(11));
        token2.setExpiredTime(LocalDateTime.now().minusMinutes(1));
        token2 = tokenRepository.save(token2);

        //when
        tokenService.expireToken();

        //then
        token1 = tokenRepository.findByTokenKey(token1.getTokenKey());
        token2 = tokenRepository.findByTokenKey(token2.getTokenKey());

        assertEquals(Token.TokenState.EXPIRED, token1.getState());
        assertEquals(Token.TokenState.WAITING, token2.getState());

    }


    @Test
    void 토큰활성화_체크() {
        //given
        User user = new User();
        user.setId(2);
        user.setName("유저2" );
        user = userRepository.save(user);

        User user2 = new User();
        user2.setId(3);
        user2.setName("유저3" );
        user2 = userRepository.save(user2);

        User user3 = new User();
        user3.setId(4);
        user3.setName("유저3" );
        user3 = userRepository.save(user3);

        Token token1 = new Token();
        token1.setTokenKey(UUID.randomUUID());
        token1.setState(Token.TokenState.EXPIRED);
        token1.setUser(user);
        token1.setCreatedTime(LocalDateTime.now().minusMinutes(13));
        token1.setExpiredTime(LocalDateTime.now().minusMinutes(3));
        token1 = tokenRepository.save(token1);

        Token token2 = new Token();
        token2.setTokenKey(UUID.randomUUID());
        token2.setState(Token.TokenState.WAITING);
        token2.setUser(user2);
        token2.setCreatedTime(LocalDateTime.now());
        token2.setExpiredTime(LocalDateTime.now().plusMinutes(10));
        token2 = tokenRepository.save(token2);

        Token token3 = new Token();
        token3.setTokenKey(UUID.randomUUID());
        token3.setState(Token.TokenState.ACTIVATE);
        token3.setUser(user3);
        token3.setCreatedTime(LocalDateTime.now().minusMinutes(1));
        token3.setExpiredTime(LocalDateTime.now().plusMinutes(9));
        token3 = tokenRepository.save(token3);

        //when
        boolean token1State = tokenService.checkTokenActivate(token1.getTokenKey());
        boolean token2State = tokenService.checkTokenActivate(token2.getTokenKey());
        boolean token3State = tokenService.checkTokenActivate(token3.getTokenKey());

        //then
        assertEquals(false, token1State);
        assertEquals(false, token2State);
        assertEquals(true, token3State);

    }
}

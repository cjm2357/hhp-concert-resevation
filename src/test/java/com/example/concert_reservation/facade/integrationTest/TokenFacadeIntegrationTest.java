package com.example.concert_reservation.facade.integrationTest;

import com.example.concert_reservation.application.token.TokenFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.point.PointRepository;
import com.example.concert_reservation.domain.token.TokenRepository;
import com.example.concert_reservation.domain.user.UserRepository;
import com.example.concert_reservation.fixture.TokenFixture;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.infra.token.TokenRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TokenFacadeIntegrationTest {

    @Autowired
    TokenFacade tokenFacade;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRedisRepository tokenRedisRepository;

    @Autowired
    RedisTemplate redisTemplate;

    @BeforeEach
    void init() {

        //redis data 초기화
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        User user = UserFixture.createUser(1, "유저1", 1, 10000l);
        pointRepository.save(user.getPoint());
        userRepository.save(user);

    }

    @Test
    void 토큰발급후_대기() {
        //given
        Integer userId = 1;

        //when
        Token token = tokenFacade.createToken(userId);

        //then
        assertEquals(userId, token.getUser().getId());
        assertNotEquals(0, token.getOrder());
    }

    @Test
    void 토큰발급_실패_유저() {
        //given
        Integer userId = -1;

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            tokenFacade.createToken(userId);
        });

        //then
        assertEquals(CustomExceptionCode.USER_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.USER_NOT_FOUND.getMessage().toString(), exception.getCustomExceptionCode().getMessage());

    }

    @Test
    void 토큰상태_조회_성공() {
        //given
        Integer userId = 1;
        Token tokenInfo = tokenFacade.createToken(userId);

        //when
        Token token = tokenFacade.getTokenStatus(tokenInfo.getTokenKey());

        //then
        assertEquals(userId, tokenInfo.getUser().getId());
        assertEquals(Token.TokenState.WAITING, token.getState());

    }

    @Test
    void 토큰상태변경및_조회_실패_토큰정보없음() {
        //given
        UUID tokenKey = UUID.randomUUID();

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            tokenFacade.getTokenStatus(tokenKey);
        });

        //then
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getMessage().toString(), exception.getCustomExceptionCode().getMessage());

    }

    @Test
    void 토큰상태변경및_조회_실패_만료된토큰() {
        //given
        UUID tokenKey = UUID.randomUUID();
        User user = userRepository.findById(1);
        Token token = TokenFixture.createToken(null, user, tokenKey, LocalDateTime.now().minusMinutes(20), Token.TokenState.EXPIRED);

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            tokenFacade.getTokenStatus(tokenKey);
        });

        //then
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getMessage().toString(), exception.getCustomExceptionCode().getMessage());

    }


    @Test
    void 토큰50개이상_대기번호_발급 () {
        //given

        for (int i = 1; i< 61; i++) {

            User user = new User();
            user.setId(i+1);
            user.setName("유저" +(i+1));
            userRepository.save(user);
            tokenFacade.createToken(i);
        }

        int userId = 61;

        //when
        Token token = tokenFacade.createToken(userId);

        //then
        assertEquals(userId, token.getUser().getId());
        assertEquals(token.getOrder(), 61);
    }

    @Test
    void ACTIVATE_토큰만료시키기_성공() {
        //given
        User user = new User();
        user.setId(2);
        user.setName("유저2" );
        user = userRepository.save(user);

        User user2 = new User();
        user2.setId(3);
        user2.setName("유저3" );
        user2 = userRepository.save(user2);

        Token token1 = TokenFixture.createToken(null, user, UUID.randomUUID(), LocalDateTime.now(),null);
        Token token2 = TokenFixture.createToken(null, user2, UUID.randomUUID(), LocalDateTime.now(),null);

        token1 = tokenRedisRepository.createToken(token1);
        token2 = tokenRedisRepository.createToken(token2);
        tokenRedisRepository.activateTokens(2);

        //when
        tokenFacade.expireToken(token1.getTokenKey());
        tokenFacade.expireToken(token2.getTokenKey());

        //then
        UUID tokenKey1 =token1.getTokenKey();
        UUID tokenKey2 =token2.getTokenKey();

        CustomException exception1 = assertThrows(CustomException.class, () -> {
            tokenFacade.getTokenStatus(tokenKey1);
        });
        CustomException exception2 = assertThrows(CustomException.class, () -> {
            tokenFacade.getTokenStatus(tokenKey2);
        });

        //then
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getStatus(), exception1.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getMessage().toString(), exception1.getCustomExceptionCode().getMessage());
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getStatus(), exception2.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getMessage().toString(), exception2.getCustomExceptionCode().getMessage());

    }


    @Test
    void WAITING_토큰만료시키지_않기_성공() {
        //given
        User user = new User();
        user.setId(2);
        user.setName("유저2" );
        user = userRepository.save(user);

        User user2 = new User();
        user2.setId(3);
        user2.setName("유저3" );
        user2 = userRepository.save(user2);

        Token token1 = TokenFixture.createToken(null, user, UUID.randomUUID(), LocalDateTime.now(),null);
        Token token2 = TokenFixture.createToken(null, user2, UUID.randomUUID(), LocalDateTime.now(),null);

        token1 = tokenRedisRepository.createToken(token1);
        token2 = tokenRedisRepository.createToken(token2);

        //when
        tokenFacade.expireToken(token1.getTokenKey());
        tokenFacade.expireToken(token2.getTokenKey());

        //then
        token1 = tokenFacade.getTokenStatus(token1.getTokenKey());
        token2 = tokenFacade.getTokenStatus(token2.getTokenKey());

        assertEquals(Token.TokenState.WAITING, token1.getState());
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

        Token token1 = TokenFixture.createToken(null, user, UUID.randomUUID(), LocalDateTime.now(),null);
        Token token2 = TokenFixture.createToken(null, user2, UUID.randomUUID(), LocalDateTime.now(),null);

        token1 = tokenRedisRepository.createToken(token1);
        token2 = tokenRedisRepository.createToken(token2);
        tokenRedisRepository.activateTokens(1);

        //when
        token1 = tokenFacade.getTokenStatus(token1.getTokenKey());
        token2 = tokenFacade.getTokenStatus(token2.getTokenKey());

        //then
        assertEquals(Token.TokenState.ACTIVATE, token1.getState());
        assertEquals(Token.TokenState.WAITING, token2.getState());

    }

}

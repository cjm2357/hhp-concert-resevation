package com.example.concert_reservation.facade.integrationTest;

import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.service.repository.PointRepository;
import com.example.concert_reservation.domain.service.repository.TokenRepository;
import com.example.concert_reservation.domain.service.repository.UserRepository;
import com.example.concert_reservation.fixture.TokenFixture;
import com.example.concert_reservation.fixture.UserFixture;
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
public class TokenFacadeIntegrationTest {

    @Autowired
    TokenFacade tokenFacade;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        User user = UserFixture.createUser(1, "유저1", 1, 10000l);
        pointRepository.save(user.getPoint());
        userRepository.save(user);
    }

    @Test
    void 토큰발급후_대기없을때_바로_active() {
        //given
        Integer userId = 1;

        //when
        Token token = tokenFacade.getToken(userId);

        //then
        assertEquals(userId, token.getUser().getId());
        assertEquals(0, token.getOrder());
    }

    @Test
    void 토큰상태변경및_조회() {
        //given
        Integer userId = 1;
        Token tokenInfo = tokenFacade.getToken(userId);

        //when
        Token token = tokenFacade.getTokenStatusAndUpdate(tokenInfo.getTokenKey());

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
            tokenFacade.getToken(i);
        }

        int userId = 61;

        //when
        Token token = tokenFacade.getToken(userId);

        //then
        assertEquals(userId, token.getUser().getId());
        assertEquals(token.getOrder(), 61-50);
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

        Token token1 = TokenFixture.createToken(null, user, UUID.randomUUID(), LocalDateTime.now().minusMinutes(13), Token.TokenState.ACTIVATE);
        tokenRepository.save(token1);

        Token token2 = TokenFixture.createToken(null, user2, UUID.randomUUID(), LocalDateTime.now().minusMinutes(11), Token.TokenState.ACTIVATE);
        tokenRepository.save(token2);

        //when
        tokenFacade.expireToken();

        //then
        token1 = tokenRepository.findByTokenKey(token1.getTokenKey());
        token2 = tokenRepository.findByTokenKey(token2.getTokenKey());

        assertEquals(Token.TokenState.EXPIRED, token1.getState());
        assertEquals(Token.TokenState.EXPIRED, token2.getState());

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

        Token token1 = TokenFixture.createToken(null, user, UUID.randomUUID(), LocalDateTime.now().minusMinutes(13), Token.TokenState.WAITING);
        tokenRepository.save(token1);

        Token token2 = TokenFixture.createToken(null, user2, UUID.randomUUID(), LocalDateTime.now().minusMinutes(11), Token.TokenState.WAITING);
        tokenRepository.save(token2);

        //when
        tokenFacade.expireToken();

        //then
        token1 = tokenRepository.findByTokenKey(token1.getTokenKey());
        token2 = tokenRepository.findByTokenKey(token2.getTokenKey());

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

        User user3 = new User();
        user3.setId(4);
        user3.setName("유저4" );
        user3 = userRepository.save(user3);

        Token token1 = TokenFixture.createToken(null, user, UUID.randomUUID(), LocalDateTime.now().minusMinutes(13), Token.TokenState.EXPIRED);
        token1 = tokenRepository.save(token1);

        Token token2 = TokenFixture.createToken(null, user2, UUID.randomUUID(), LocalDateTime.now().minusMinutes(10), Token.TokenState.WAITING);
        token2 = tokenRepository.save(token2);

        Token token3 = TokenFixture.createToken(null, user3, UUID.randomUUID(), LocalDateTime.now().minusMinutes(1), Token.TokenState.ACTIVATE);
        token3 = tokenRepository.save(token3);

        //when
        token1 = tokenFacade.getTokenInfo(token1.getTokenKey());
        token2 = tokenFacade.getTokenInfo(token2.getTokenKey());
        token3 = tokenFacade.getTokenInfo(token3.getTokenKey());

        //then
        assertEquals(Token.TokenState.EXPIRED, token1.getState());
        assertEquals(Token.TokenState.WAITING, token2.getState());
        assertEquals(Token.TokenState.ACTIVATE, token3.getState());

    }
}

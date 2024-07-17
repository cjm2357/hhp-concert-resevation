package com.example.concert_reservation.service.unitTest.facade;


import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.fixture.TokenFixture;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenFacadeUnitTest {

    @Mock
    TokenService tokenService;

    @InjectMocks
    TokenFacade tokenFacade;

    @Test
    void 토큰발급_대기없음() {
        //given
        User user = UserFixture.createUser(1, "user1", 1, 10000l);
        Token expectedToken = TokenFixture.createToken(1, user, UUID.randomUUID(), LocalDateTime.now(), Token.TokenState.ACTIVATE);
        when(tokenService.getToken(any())).thenReturn(expectedToken);

        //when
        Token token = tokenFacade.getToken(user.getId());
        token.setOrder(0);

        //then
        assertEquals(user.getId(), token.getUser().getId());
        assertEquals(0, token.getOrder());
    }

    @Test
    void 토큰발급_대기있음() {
        //given
        List<Token> activatedTokens = new ArrayList<>();

        for (int i = 1; i< 51; i++) {
            User u = UserFixture.createUser(i, "user"+i, i, 1000l);
            Token t = TokenFixture.createToken(i, u, UUID.randomUUID(), LocalDateTime.now(), Token.TokenState.ACTIVATE);
            activatedTokens.add(t);
        }

        User user = UserFixture.createUser(60, "user60", 60, 1000l);
        Token expectedToken =  TokenFixture.createToken(60, user, UUID.randomUUID(), LocalDateTime.now(), Token.TokenState.WAITING);

        when(tokenService.getToken(any())).thenReturn(expectedToken);

        //when
        Token token = tokenFacade.getToken(user.getId());
        token.setOrder(10);

        //then
        assertEquals(user.getId(), token.getUser().getId());
        assertEquals(10, token.getOrder());
    }


    @Test
    void 토큰상태_조회_waiting() {
        //given
        Integer userId = 1;

        User user = new User();
        user.setId(userId);

        Token searchedToken = TokenFixture.createToken(60, user, UUID.randomUUID(), LocalDateTime.now().minusMinutes(15), Token.TokenState.WAITING);

        UUID expectedTokenKey = UUID.randomUUID();
        Token expectedToken = TokenFixture.createToken(60, user, expectedTokenKey, LocalDateTime.now().minusMinutes(15), Token.TokenState.WAITING);
        expectedToken.setOrder(60-50);


        when(tokenService.getTokenStatusAndUpdate(any())).thenReturn(expectedToken);

        //when
        Token token = tokenFacade.getTokenStatusAndUpdate(searchedToken.getTokenKey());

        //then
        assertEquals(60- 50, token.getOrder());
        assertEquals(expectedTokenKey, token.getTokenKey());

    }

    @Test
    void 토큰상태_조회_activate() {
        //given
        Integer userId = 1;

        User user = new User();
        user.setId(userId);


        Token curToken = TokenFixture.createToken(70, user, UUID.randomUUID(), LocalDateTime.now().minusMinutes(15), Token.TokenState.ACTIVATE);
        curToken.setOrder(0);

        when(tokenService.getTokenStatusAndUpdate(any())).thenReturn(curToken);

        //when
        Token token = tokenFacade.getTokenStatusAndUpdate(curToken.getTokenKey());
        token.setOrder(0);

        //then
        assertEquals(0, token.getOrder());
        assertEquals(curToken.getTokenKey(), token.getTokenKey());

    }

    @Test
    void 유효하지않은_토큰키() {

        //when
        Token token = tokenFacade.getTokenStatusAndUpdate(null);

        //then
        assertNull(token);

    }
}
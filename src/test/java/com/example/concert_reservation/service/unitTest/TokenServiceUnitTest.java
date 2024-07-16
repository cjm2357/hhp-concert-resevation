package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.fixture.TokenFixture;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.service.TokenService;
import com.example.concert_reservation.service.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceUnitTest {

    @Mock
    TokenRepository tokenRepository;

    @InjectMocks
    TokenService tokenService;

    @Test
    void 토큰발급_대기없음() {
        //given
        User user = UserFixture.createUser(1, "user1", 1, 10000l);
        Token expectedToken = TokenFixture.createToken(1, user, UUID.randomUUID(), LocalDateTime.now(), Token.TokenState.ACTIVATE);
        when(tokenRepository.save(any())).thenReturn(expectedToken);

        //when
        Token token = tokenService.getToken(user.getId());

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

        when(tokenRepository.findByStateOrderById(any())).thenReturn(activatedTokens);
        when(tokenRepository.save(any())).thenReturn(expectedToken);

        //when
        Token token = tokenService.getToken(user.getId());

        //then
        assertEquals(user.getId(), token.getUser().getId());
        assertEquals(10, token.getOrder());
    }

    @Test
    void 유효하지않은_유저ID() {
        //given
        Integer userId = null;

        //when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            tokenService.getToken(userId);
        });


        //then
        assertEquals("유효하지않은 userId 입니다.", exception.getMessage().toString());


    }

    @Test
    void 토큰상태_조회_waiting() {
        //given
        Integer userId = 1;

        User user = new User();
        user.setId(userId);

        Token searchedToken = new Token();
        searchedToken.setId(60);
        searchedToken.setTokenKey(UUID.randomUUID());
        searchedToken.setCreatedTime(LocalDateTime.now().minusMinutes(1));
        searchedToken.setCreatedTime(LocalDateTime.now().plusMinutes(1));

        List<Token> activatedTokens = new ArrayList<>();

        for (int i = 1; i< 51; i++) {
            Token t = new Token();
            t.setId(i);
            t.setTokenKey(UUID.randomUUID());
            activatedTokens.add(t);
        }

        Token expectedToken = new Token();
        expectedToken.setUser(user);
        expectedToken.setId(60);
        expectedToken.setTokenKey(UUID.randomUUID());
        expectedToken.setState(Token.TokenState.WAITING);
        expectedToken.setCreatedTime(LocalDateTime.now().minusMinutes(15));
        expectedToken.setExpiredTime(LocalDateTime.now().minusMinutes(5));

        when(tokenRepository.findByTokenKey(any())).thenReturn(searchedToken);
        when(tokenRepository.findByStateOrderById(any())).thenReturn(activatedTokens);
        when(tokenRepository.save(any())).thenReturn(expectedToken);

        //when
        Token token = tokenService.getTokenStatusAndUpdate(searchedToken.getTokenKey());

        //then
        assertEquals(60- 50, expectedToken.getOrder());
        assertEquals(token.getTokenKey(), expectedToken.getTokenKey());

    }

    @Test
    void 토큰상태_조회_activate() {
        //given
        Integer userId = 1;

        User user = new User();
        user.setId(userId);


        Token curToken = new Token();
        curToken.setUser(user);
        curToken.setId(70);
        curToken.setTokenKey(UUID.randomUUID());
        curToken.setState(Token.TokenState.ACTIVATE);
        curToken.setCreatedTime(LocalDateTime.now().minusMinutes(15));
        curToken.setExpiredTime(LocalDateTime.now().minusMinutes(5));

        when(tokenRepository.findByTokenKey(any())).thenReturn(curToken);

        //when
        Token token = tokenService.getTokenStatusAndUpdate(curToken.getTokenKey());

        //then
        assertEquals(0, token.getOrder());
        assertEquals(curToken.getTokenKey(), token.getTokenKey());

    }

    @Test
    void 유효하지않은_토큰키() {

        //when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            tokenService.getTokenStatusAndUpdate(null);
        });

        //then
        assertEquals("유효하지않은 토큰 key 입니다.", exception.getMessage().toString());

    }
}

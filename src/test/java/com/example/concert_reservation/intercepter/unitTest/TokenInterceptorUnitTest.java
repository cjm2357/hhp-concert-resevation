package com.example.concert_reservation.intercepter.unitTest;

import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.fixture.TokenFixture;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.presentation.interceptor.TokenInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpRequest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenInterceptorUnitTest {

    @Mock
    TokenFacade tokenFacade;

    @InjectMocks
    TokenInterceptor tokenInterceptor;

    @Test
    void 인터셉터_패싱() throws Exception{
        //given
        UUID tokenKey = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn(tokenKey.toString());
        User user = UserFixture.createUser(1, "user1", 1, 100l);
        Token token = TokenFixture.createToken(1, user,tokenKey, LocalDateTime.now(), Token.TokenState.ACTIVATE);
        when(tokenFacade.getTokenInfo(tokenKey)).thenReturn(token);

        //when
        boolean isPass = tokenInterceptor.preHandle(request, response, new Object());

        //then
        assertTrue(isPass);

    }

    @Test
    void 인터셉터_에러_Authorization_UUID_아님() throws Exception{
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("AA");

        //then
        CustomException exception = assertThrows(CustomException.class, () -> {
            tokenInterceptor.preHandle(request, response, new Object());
        });

        //then
        assertEquals(CustomExceptionCode.INVALID_TOKEN_KEY.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.INVALID_TOKEN_KEY.getMessage().toString(), exception.getCustomExceptionCode().getMessage());
    }

    @Test
    void 인터셉터_에러_Authorization_토큰정보_없음() throws Exception{
        //given
        UUID tokenKey = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn(tokenKey.toString());
        when(tokenFacade.getTokenInfo(tokenKey)).thenReturn(null);

        //then
        CustomException exception = assertThrows(CustomException.class, () -> {
            tokenInterceptor.preHandle(request, response, new Object());
        });

        //then
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.TOKEN_NOT_FOUND.getMessage().toString(), exception.getCustomExceptionCode().getMessage());
    }

    @Test
    void 인터셉터_에러_Authorization_토큰만료() throws Exception{
        //given
        UUID tokenKey = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn(tokenKey.toString());
        User user = UserFixture.createUser(1, "user1", 1, 100l);
        Token token = TokenFixture.createToken(1, user,tokenKey, LocalDateTime.now(), Token.TokenState.EXPIRED);
        when(tokenFacade.getTokenInfo(tokenKey)).thenReturn(token);

        //then
        CustomException exception = assertThrows(CustomException.class, () -> {
            tokenInterceptor.preHandle(request, response, new Object());
        });

        //then
        assertEquals(CustomExceptionCode.TOKEN_NOT_VALID.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.TOKEN_NOT_VALID.getMessage().toString(), exception.getCustomExceptionCode().getMessage());
    }

}
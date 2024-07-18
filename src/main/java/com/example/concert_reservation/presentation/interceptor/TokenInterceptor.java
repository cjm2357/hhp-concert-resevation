package com.example.concert_reservation.presentation.interceptor;

import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final TokenFacade tokenFacade;

    public TokenInterceptor (TokenFacade tokenFacade) {
        this.tokenFacade = tokenFacade;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UUID tokenKey = null;
        try {
            tokenKey = UUID.fromString(request.getHeader("Authorization"));
        } catch (IllegalArgumentException e) {
            log.warn("request failed, token key is invalid");
            throw new CustomException(CustomExceptionCode.INVALID_TOKEN_KEY);
        }

        if (tokenKey == null) {
            log.warn("request failed. token key is null");
            throw new CustomException(CustomExceptionCode.NO_TOKEN_KEY);
        }
        Token token = tokenFacade.getTokenInfo(tokenKey);

        if (token.getState() != Token.TokenState.ACTIVATE) {
            log.warn("Request failed. status of token is {}", token.getState());
            throw new CustomException(CustomExceptionCode.TOKEN_NOT_VALID);
        }

        return true;

    }
}

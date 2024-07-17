package com.example.concert_reservation.presentation.interceptor;

import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.sasl.AuthenticationException;
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
        UUID tokenKey = UUID.fromString(request.getHeader("Authorization"));
        Token token = tokenFacade.getTokenInfo(tokenKey);

        if (token.getState() != Token.TokenState.ACTIVATE) {
            log.info("Request failed. status of token is {}", token.getState());
            throw new AuthenticationException("Token이 " + token.getState() + " 상태입니다.");
        }

        return true;

    }
}

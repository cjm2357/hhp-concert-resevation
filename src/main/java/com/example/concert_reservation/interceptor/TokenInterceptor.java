package com.example.concert_reservation.interceptor;

import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public TokenInterceptor (TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UUID tokenKey = UUID.fromString(request.getHeader("Authorization"));
        Token token = tokenService.getTokenInfo(tokenKey);

        if (token.getState() != Token.TokenState.ACTIVATE) {
            log.info("Request failed. status of token is {}", token.getState());
            throw new RuntimeException("Token이 " + token.getState() + " 상태입니다.");
        }

        return true;

    }
}

package com.example.concert_reservation.fixture;

import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenFixture {
    public static Token createToken(Integer id, User user, UUID key, LocalDateTime createTime, Token.TokenState state) {
        return Token.builder()
                .id(id)
                .user(user)
                .key(key)
                .createdTime(createTime)
                .expiredTime(createTime.plusMinutes(Token.EXPIRED_TIME_TEN_MIN))
                .state(state)
                .build();
    }
}

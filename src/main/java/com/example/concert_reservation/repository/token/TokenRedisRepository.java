package com.example.concert_reservation.repository.token;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TokenRedisRepository {

    public static final String WAITING_KEY = "waitingTokens";
    public static final String PRE_ACTIVE_KEY = "active:";
    private final RedisTemplate redisTemplate;

    public Token createToken(Token token) {

        redisTemplate.opsForZSet().add(WAITING_KEY, token.getTokenKey().toString(), System.currentTimeMillis());
        //순서 조회
        int order = redisTemplate.opsForZSet().rank(WAITING_KEY, token.getTokenKey().toString()).intValue();
        token.setOrder(order+1);
        return token;
    }

    public Token getTokenStatus(UUID key) {

        //waitTokens에 있을때
        Long order = redisTemplate.opsForZSet().rank(WAITING_KEY, key.toString());
        if (order != null) {
            Token token = Token.builder()
                    .key(key)
                    .state(Token.TokenState.WAITING)
                    .build();
            //맨앞은 0이기 때문에 +1
            token.setOrder(order.intValue() + 1);
            return token;
        }

        // activeTokens에 있을 때
        if (redisTemplate.opsForValue().get(PRE_ACTIVE_KEY + key) != null) {
            Token token = Token.builder()
                    .key(key)
                    .state(Token.TokenState.ACTIVATE)
                    .build();
            token.setOrder(0);
            return token;
        }

        // 둘 다없으면 에러
        throw new CustomException(CustomExceptionCode.TOKEN_NOT_FOUND);

    }

    public void activateTokens(Integer activateCount) {
        int lastIndex = activateCount -1;
        Set<String> tokens = redisTemplate.opsForZSet().range(WAITING_KEY, 0, lastIndex);
        tokens.forEach(token ->{
            redisTemplate.opsForZSet().removeRange(WAITING_KEY,0, lastIndex);
            redisTemplate.opsForValue().set(PRE_ACTIVE_KEY + token, token);
            //ttl 을걸어 만료되면 자동삭제
            redisTemplate.expire(PRE_ACTIVE_KEY + token, Token.EXPIRED_TIME_TEN_MIN, TimeUnit.MINUTES);
        });
    }

    public void expireToken(UUID key) {
        redisTemplate.delete(PRE_ACTIVE_KEY + key);
    }

}

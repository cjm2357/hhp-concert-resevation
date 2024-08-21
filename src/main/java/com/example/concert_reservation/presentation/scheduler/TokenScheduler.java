package com.example.concert_reservation.presentation.scheduler;

import com.example.concert_reservation.application.token.TokenFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenScheduler {


    private final TokenFacade tokenFacade;
    private final RedissonClient redissonClient;
    private final int activateCount = 50;

    //10분마다 작동
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void changeActiveStatus() {
        log.info("start activate token scheduler");
        tokenFacade.activateTokens(activateCount);
        log.info("success activate token scheduler");
    }

}

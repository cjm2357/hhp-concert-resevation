package com.example.concert_reservation.presentation.scheduler;

import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.domain.entity.Point;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenScheduler {


    private final TokenFacade tokenFacade;
    private final RedissonClient redissonClient;

    private final String tokenScheduleLockKey = "tokenScheduleLock";
    private final int activateCount = 50;

    //10초마다 작동
    @Scheduled(fixedRate = 10 * 1000)
    public void changeActiveStatus() {

        // 분산환경에서 스케줄 중복실행방지를 위해 분산 락 사용
        RLock rLock = redissonClient.getLock(tokenScheduleLockKey);
        try {
            boolean available = rLock.tryLock(2, 5, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 이용불가");
            }
            tokenFacade.activateTokens(activateCount);

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Lock획득 실패");
        } finally {
            rLock.unlock();
        }
        
    }

}

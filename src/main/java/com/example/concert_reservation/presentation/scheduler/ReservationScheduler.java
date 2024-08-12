package com.example.concert_reservation.presentation.scheduler;


import com.example.concert_reservation.application.concert.ConcertFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReservationScheduler {


    private final ConcertFacade concertFacade;
    private final RedissonClient redissonClient;

    private final String reservationScheduleLockKey = "reservationScheduleLockKey";

    //10초마다 작동
//    @Scheduled(cron = "*/10 * * * * *")
    public void expireReservation() {

        // 분산환경에서 스케줄 중복실행방지를 위해 분산 락 사용
        log.info("start expire reservation scheduler");
        RLock rLock = redissonClient.getLock(reservationScheduleLockKey);
        try {
            boolean available = rLock.tryLock(2, 5, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 이용불가");
            }
            concertFacade.expireReservation();
            log.info("success expire reservation scheduler");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Lock획득 실패");
        } finally {
            rLock.unlock();
        }
    }
}

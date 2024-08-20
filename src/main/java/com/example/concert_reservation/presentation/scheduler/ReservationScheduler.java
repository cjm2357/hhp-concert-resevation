package com.example.concert_reservation.presentation.scheduler;


import com.example.concert_reservation.application.concert.ConcertFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReservationScheduler {


    private final ConcertFacade concertFacade;


    //30초마다 작동
    @Scheduled(fixedRate = 30 * 1000)
    public void expireReservation() {
        log.info("start expire reservation scheduler");
        concertFacade.expireReservation();
        log.info("success expire reservation scheduler");

    }
}

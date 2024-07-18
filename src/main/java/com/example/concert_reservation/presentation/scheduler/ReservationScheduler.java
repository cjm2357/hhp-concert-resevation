package com.example.concert_reservation.presentation.scheduler;


import com.example.concert_reservation.application.ConcertFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {

    @Autowired
    ConcertFacade concertFacade;

    //30초마다 작동
    @Scheduled(fixedRate = 30 * 1000)
    public void expireReservation() {
        concertFacade.expireReservation();
    }
}

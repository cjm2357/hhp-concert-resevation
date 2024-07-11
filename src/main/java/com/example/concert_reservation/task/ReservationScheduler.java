package com.example.concert_reservation.task;


import com.example.concert_reservation.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {

    @Autowired
    SeatService seatService;

    //30초마다 작동
    @Scheduled(fixedRate = 30 * 1000)
    public void expireReservation() {
        seatService.expireReservation();
    }
}

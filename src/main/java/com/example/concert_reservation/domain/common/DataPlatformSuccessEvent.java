package com.example.concert_reservation.domain.common;

import com.example.concert_reservation.domain.entity.Reservation;

public class DataPlatformSuccessEvent {

    private final Reservation reservation;

    public DataPlatformSuccessEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }


}

package com.example.concert_reservation.domain.common;

import com.example.concert_reservation.domain.entity.Reservation;

public class DataPlatformFailEvent {

    private final Reservation reservation;

    public DataPlatformFailEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }


}

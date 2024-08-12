package com.example.concert_reservation.domain.common;

import com.example.concert_reservation.domain.entity.Reservation;

public interface DataPlatform {

    void sendMessage(String text, Reservation reservation);
}

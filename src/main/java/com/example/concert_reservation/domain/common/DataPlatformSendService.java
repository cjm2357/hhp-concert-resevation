package com.example.concert_reservation.domain.common;

import com.example.concert_reservation.domain.entity.Reservation;

public interface DataPlatformSendService {

    void sendMessage(String text, Reservation reservation);
}

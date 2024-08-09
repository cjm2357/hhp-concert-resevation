package com.example.concert_reservation.domain.payment;

import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.entity.User;

import java.util.UUID;

public class PaymentSuccessEvent {

    private final User user;
    private final Reservation reservation;
    private final UUID tokenKey;

    public PaymentSuccessEvent(User user, Reservation reservation, UUID tokenKey) {
        this.user = user;
        this.reservation = reservation;
        this.tokenKey = tokenKey;
    }

    public User getUser() {
        return user;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public UUID getTokenKey() {
        return tokenKey;
    }
}

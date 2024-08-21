package com.example.concert_reservation.domain.payment.event;

import com.example.concert_reservation.domain.entity.Payment;
import com.example.concert_reservation.domain.entity.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PaymentEvent {

    private Integer paymentId;
    private Integer userId;
    private Integer reservationId;

    private Long price;
    private UUID tokenKey;

    public PaymentEvent(Payment payment, Reservation reservation, UUID tokenKey) {
        this.paymentId = payment.getId();
        this.userId = payment.getUserId();
        this.reservationId = payment.getReservationId();
        this.price = reservation.getPrice();
        this.tokenKey = tokenKey;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public UUID getTokenKey() {
        return tokenKey;
    }
}

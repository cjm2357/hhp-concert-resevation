package com.example.concert_reservation.dto;

import com.example.concert_reservation.domain.entity.Payment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {

    private Integer reservationId;
    private Integer userId;

    public Payment toEntity() {
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setReservationId(reservationId);
        return payment;
    }
}

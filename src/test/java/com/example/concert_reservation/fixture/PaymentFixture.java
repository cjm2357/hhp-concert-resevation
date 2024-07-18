package com.example.concert_reservation.fixture;

import com.example.concert_reservation.domain.entity.Payment;

import java.time.LocalDateTime;

public class PaymentFixture {

    public static Payment createPayment(Integer id, Integer userId, Integer reservationId, LocalDateTime createTime) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setUserId(userId);
        payment.setReservationId(reservationId);
        payment.setCreatedTime(createTime);
        return payment;
    }
}

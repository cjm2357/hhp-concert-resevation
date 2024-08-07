package com.example.concert_reservation.domain.payment;

import com.example.concert_reservation.domain.entity.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);

}

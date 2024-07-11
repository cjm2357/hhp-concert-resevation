package com.example.concert_reservation.repository.impl;


import com.example.concert_reservation.entity.Payment;
import com.example.concert_reservation.repository.PaymentJpaRepository;
import com.example.concert_reservation.service.repository.PaymentRepository;
import org.springframework.stereotype.Repository;


@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    public Payment save(Payment payment){
        return paymentJpaRepository.save(payment);
    }

}

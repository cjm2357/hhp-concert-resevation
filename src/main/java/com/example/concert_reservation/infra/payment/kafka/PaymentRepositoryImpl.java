package com.example.concert_reservation.infra.payment.kafka;


import com.example.concert_reservation.domain.entity.Payment;
import com.example.concert_reservation.domain.payment.PaymentRepository;
import com.example.concert_reservation.infra.payment.PaymentJpaRepository;
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

    public void deleteById(Integer paymentId) {
        paymentJpaRepository.deleteById(paymentId);
    }

}

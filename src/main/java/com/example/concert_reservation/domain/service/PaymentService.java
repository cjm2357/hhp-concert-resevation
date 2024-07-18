package com.example.concert_reservation.domain.service;

import com.example.concert_reservation.domain.entity.Payment;
import com.example.concert_reservation.domain.service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository)
    {
        this.paymentRepository = paymentRepository;
    }


    public Payment pay(Payment payment) {
        return paymentRepository.save(payment);
    }


}

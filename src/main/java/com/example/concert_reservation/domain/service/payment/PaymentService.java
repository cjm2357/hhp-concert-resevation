package com.example.concert_reservation.domain.service.payment;

import com.example.concert_reservation.domain.entity.Payment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository)
    {
        this.paymentRepository = paymentRepository;
    }


    public Payment pay(Payment payment) {
        payment.setCreatedTime(LocalDateTime.now());
        return paymentRepository.save(payment);
    }


}

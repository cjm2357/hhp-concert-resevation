package com.example.concert_reservation.domain.payment;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository)
    {
        this.paymentRepository = paymentRepository;
    }


    public Payment pay(Payment payment) {
        payment.setCreatedTime(LocalDateTime.now());
        try {
            return paymentRepository.save(payment);
        } catch (CustomException e) {
            log.warn("user {} reservation {} payment is failed", payment.getUserId(), payment.getReservationId());
            throw new CustomException(CustomExceptionCode.PAYMENT_IS_FAILED);
        }
    }


}

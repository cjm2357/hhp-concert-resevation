package com.example.concert_reservation.infra.payment.message;

import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentJpaOutboxWriter extends JpaRepository<PaymentMessage, Integer> {

    List<PaymentMessage> findAllByState(PaymentMessage.PaymentState state);

}

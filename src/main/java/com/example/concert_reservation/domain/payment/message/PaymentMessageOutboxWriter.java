package com.example.concert_reservation.domain.payment.message;

import com.example.concert_reservation.infra.payment.message.PaymentState;

import java.util.List;

public interface PaymentMessageOutboxWriter {

    PaymentMessage create(PaymentMessage paymentMessage);
    PaymentMessage update(PaymentMessage paymentMessage);

    List<PaymentMessage> findAllByState(PaymentState state);

}

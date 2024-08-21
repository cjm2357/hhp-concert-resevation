package com.example.concert_reservation.domain.payment.message;

import java.util.List;

public interface PaymentMessageOutboxWriter {

    PaymentMessage create(PaymentMessage paymentMessage);
    PaymentMessage update(PaymentMessage paymentMessage);

    List<PaymentMessage> findAllByState(PaymentMessage.PaymentState state);

}

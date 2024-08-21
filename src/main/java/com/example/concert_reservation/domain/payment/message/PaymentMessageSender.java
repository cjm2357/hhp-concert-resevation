package com.example.concert_reservation.domain.payment.message;

public interface PaymentMessageSender {

    void send(PaymentMessage paymentMessage);
}

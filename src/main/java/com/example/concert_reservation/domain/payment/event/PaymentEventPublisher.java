package com.example.concert_reservation.domain.payment.event;

public interface PaymentEventPublisher {

    public void success(PaymentEvent event);
}

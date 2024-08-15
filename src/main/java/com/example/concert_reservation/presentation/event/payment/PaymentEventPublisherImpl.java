package com.example.concert_reservation.presentation.event.payment;

import com.example.concert_reservation.domain.payment.event.PaymentEventPublisher;
import com.example.concert_reservation.domain.payment.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void success(PaymentEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}

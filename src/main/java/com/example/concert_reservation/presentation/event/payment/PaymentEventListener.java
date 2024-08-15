package com.example.concert_reservation.presentation.event.payment;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.payment.event.PaymentEvent;
import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import com.example.concert_reservation.domain.payment.message.PaymentMessageOutboxWriter;
import com.example.concert_reservation.domain.payment.message.PaymentMessageSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentMessageSender paymentMessageSender;
    private final PaymentMessageOutboxWriter paymentMessageOutboxWriter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutBoxMessage(PaymentEvent event) {

        try {
            PaymentMessage paymentMessage = new PaymentMessage(event);
            String json =objectMapper.writeValueAsString(event);
            paymentMessage.setMessage(json);
            paymentMessage.init();
            paymentMessageOutboxWriter.create(paymentMessage);
        } catch (JsonProcessingException e) {
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessage(PaymentEvent event) {
        try {
            PaymentMessage paymentMessage = new PaymentMessage(event);
            String json = objectMapper.writeValueAsString(event);
            paymentMessage.setMessage(json);
            paymentMessage.init();
            paymentMessageSender.send(paymentMessage);
        } catch (JsonProcessingException e) {
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        }

    }

}

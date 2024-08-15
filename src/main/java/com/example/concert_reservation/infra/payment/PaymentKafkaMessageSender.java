package com.example.concert_reservation.infra.payment;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import com.example.concert_reservation.domain.payment.message.PaymentMessageSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;



@RequiredArgsConstructor
@Component
public class PaymentKafkaMessageSender implements PaymentMessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${payment.topic}")
    private String PAYMENT_TOPIC;
    @Override
    public void send(PaymentMessage paymentMessage) {
        try {
            String text = objectMapper.writeValueAsString(paymentMessage);
            kafkaTemplate.send(PAYMENT_TOPIC, text);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new CustomException(CustomExceptionCode.INVALID_OBJECT_TYPE);
        }

    }
}

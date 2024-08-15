package com.example.concert_reservation.domain.payment.message;

import com.example.concert_reservation.domain.payment.event.PaymentEvent;
import com.example.concert_reservation.infra.payment.message.PaymentState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentMessage {

    private Integer paymentId;
    private String message;

    @Enumerated(EnumType.STRING)
    private PaymentState state;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;


    public PaymentMessage() {};

    public PaymentMessage(String message) {
        this.message = message;
    }

    public PaymentMessage(PaymentEvent event) throws JsonProcessingException {
        this.paymentId = event.getPaymentId();
    }

    public void init() {
        this.state = PaymentState.INIT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    public void changeState (PaymentState state){
        this.state = state;
        this.updatedAt = LocalDateTime.now();
    }





}

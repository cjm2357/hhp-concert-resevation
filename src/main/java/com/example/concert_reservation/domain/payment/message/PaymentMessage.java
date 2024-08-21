package com.example.concert_reservation.domain.payment.message;

import com.example.concert_reservation.domain.payment.event.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Slf4j
public class PaymentMessage {

    @Id
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

    public enum PaymentState {
        INIT, PUBLISHED
    }
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

    @Builder
    public PaymentMessage(PaymentMessage paymentMessage, PaymentState state) {
        this.paymentId = paymentMessage.getPaymentId();
        this.message = paymentMessage.getMessage();
        this.state = state;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}

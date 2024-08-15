package com.example.concert_reservation.infra.payment.message;

import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Getter
@Slf4j
@NoArgsConstructor
public class PaymentOutboxEntity {


    @Id
    private Integer paymentId;
    private String message;

    @Enumerated(EnumType.STRING)
    private PaymentState state;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PaymentOutboxEntity(PaymentMessage paymentMessage, PaymentState state) {
        this.paymentId = paymentMessage.getPaymentId();
        this.message = paymentMessage.getMessage();
        this.state = state;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PaymentMessage toDomain() {
        PaymentMessage paymentMessage = new PaymentMessage();
        paymentMessage.setPaymentId(this.paymentId);
        paymentMessage.setMessage(this.message);
        paymentMessage.setState(this.state);
        paymentMessage.setCreatedAt(this.createdAt);
        paymentMessage.setUpdatedAt(this.updatedAt);
        return paymentMessage;
    }
}

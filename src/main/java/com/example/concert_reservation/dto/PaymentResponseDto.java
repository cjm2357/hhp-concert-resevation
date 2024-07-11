package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.Payment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponseDto {

    private Integer paymentId;
    private Integer userId;
    private Integer reservationId;
    private LocalDateTime createdTime;

    public PaymentResponseDto(Payment payment) {
        this.paymentId = payment.getId();
        this.userId = payment.getUserId();
        this.reservationId = payment.getUserId();
        this.createdTime = payment.getCreatedTime();
    }
}

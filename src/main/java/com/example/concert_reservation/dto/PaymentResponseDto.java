package com.example.concert_reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDto {

    private Integer reservationId;
    private boolean success;
    private String concertName;
    private Integer seatNo;
    private Long seatPrice;
}

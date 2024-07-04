package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.Seat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatReservationResponseDto {

    private boolean success;
    private Seat seat;
}

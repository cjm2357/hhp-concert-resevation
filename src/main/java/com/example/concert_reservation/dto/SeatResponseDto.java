package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.Seat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SeatResponseDto {

    private List<Seat> seats;
}

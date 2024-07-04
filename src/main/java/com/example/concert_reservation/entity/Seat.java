package com.example.concert_reservation.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {

    private Integer id;
    private Integer concertId;
    private Integer scheduleId;
    private Integer seatNo;
    private String state;
    private Long price;
    private String grade;
}

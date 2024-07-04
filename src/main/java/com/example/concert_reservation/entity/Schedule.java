package com.example.concert_reservation.entity;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Schedule {

    private Integer id;
    private Integer concertId;
    private LocalDateTime date;
}

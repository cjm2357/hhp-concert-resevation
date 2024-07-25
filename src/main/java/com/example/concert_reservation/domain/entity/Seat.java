package com.example.concert_reservation.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seat {

    public enum State {
        RESERVED, EMPTY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer concertId;
    private Integer scheduleId;
    private Integer seatNo;

    @Enumerated(EnumType.STRING)
    private State state;
    private Long price;
    private String grade;


    @Version
    private int version;
}

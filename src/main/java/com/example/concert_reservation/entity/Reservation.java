package com.example.concert_reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reservation {

    public enum State {
        COMPLETED, WAITING, EXPIRED
    }

    public static final int EXPIRE_TIME_FIVE_MIN = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer seatId;
    private Integer scheduleId;
    private Integer concertId;
    private Integer seatNo;
    private String seatGrade;
    private Long price;

    @Enumerated(EnumType.STRING)
    private State state;
    private LocalDateTime createdTime;
    private LocalDateTime expiredTime;
}

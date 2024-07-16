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

    public void enrollSeatInfoForReservation (Integer userId, Seat seat) {
        this.seatId = seat.getId();
        this.userId = userId;
        this.scheduleId = seat.getScheduleId();
        this.concertId = seat.getConcertId();
        this.seatNo = seat.getSeatNo();
        this.seatGrade = getSeatGrade();
        this.price = seat.getPrice();
        this.state = State.WAITING;
        this.createdTime = LocalDateTime.now();
        this.expiredTime = LocalDateTime.now().plusMinutes(EXPIRE_TIME_FIVE_MIN);
    }
}

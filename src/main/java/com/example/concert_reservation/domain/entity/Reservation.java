package com.example.concert_reservation.domain.entity;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Slf4j
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
        if (seat == null) {
            new CustomException(CustomExceptionCode.SEAT_NOT_FOUND);
        }
        this.seatId = seat.getId();
        this.userId = userId;
        this.scheduleId = seat.getScheduleId();
        this.concertId = seat.getConcertId();
        this.seatNo = seat.getSeatNo();
        this.seatGrade = seat.getGrade();
        this.price = seat.getPrice();
        this.state = State.WAITING;
        this.createdTime = LocalDateTime.now();
        this.expiredTime = LocalDateTime.now().plusMinutes(EXPIRE_TIME_FIVE_MIN);
    }

    public void isNotExpired() {
        if (state == Reservation.State.EXPIRED || expiredTime.isBefore(LocalDateTime.now())) {
            log.warn("{} user, payment time expired", userId);
            throw new CustomException(CustomExceptionCode.PAYMENT_TIME_EXPIRE);
        }
    }
}

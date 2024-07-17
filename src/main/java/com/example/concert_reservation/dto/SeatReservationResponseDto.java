package com.example.concert_reservation.dto;

import com.example.concert_reservation.domain.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SeatReservationResponseDto {


    private Integer id;
    private Integer userId;
    private Integer seatId;
    private Integer scheduleId;
    private Integer concertId;
    private Integer seatNo;
    private String seatGrade;
    private Long price;
    private Reservation.State state;
    private LocalDateTime createdTime;
    private LocalDateTime expiredTime;

    public SeatReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.userId = reservation.getUserId();
        this.seatId = reservation.getSeatId();
        this.scheduleId = reservation.getScheduleId();
        this.concertId = reservation.getConcertId();
        this.seatNo = reservation.getSeatNo();
        this.seatGrade = reservation.getSeatGrade();
        this.price = reservation.getPrice();
        this.createdTime = reservation.getCreatedTime();
        this.expiredTime = reservation.getExpiredTime();
    }
}

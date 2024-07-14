package com.example.concert_reservation.fixture;

import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.Seat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class ReservationFixture {
    public static Reservation creasteReservation(Integer id, Integer userId, Integer concertId, Integer seatId, Integer scheduleId, Integer seatNo, Reservation.State state, Long price, String grade, LocalDateTime createTime) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setUserId(userId);
        reservation.setConcertId(concertId);
        reservation.setSeatId(seatId);
        reservation.setScheduleId(scheduleId);
        reservation.setSeatNo(seatNo);
        reservation.setState(state);
        reservation.setSeatGrade(grade);
        reservation.setPrice(price);
        reservation.setCreatedTime(createTime);
        reservation.setExpiredTime(createTime.plusMinutes(Reservation.EXPIRE_TIME_FIVE_MIN));
        return reservation;
    }
}

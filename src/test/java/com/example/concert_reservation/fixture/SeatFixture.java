package com.example.concert_reservation.fixture;

import com.example.concert_reservation.entity.Seat;

public class SeatFixture {

    public static Seat createSeat(Integer id, Integer concertId, Integer scheduleId, Integer seatNo,  Seat.State state, Long price, String grade) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setConcertId(concertId);
        seat.setScheduleId(scheduleId);
        seat.setSeatNo(seatNo);
        seat.setState(state);
        seat.setPrice(price);
        seat.setGrade(grade);
        return seat;
    }
}

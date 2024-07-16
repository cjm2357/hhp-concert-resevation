package com.example.concert_reservation.fixture;

import com.example.concert_reservation.entity.Schedule;
import com.example.concert_reservation.entity.Seat;

import java.time.LocalDateTime;

public class ScheduleFixture {

    public static Schedule createSchedule(Integer id, Integer concertId, LocalDateTime dateTime) {
        Schedule schedule = Schedule.builder()
                .id(id)
                .concertId(concertId)
                .date(dateTime)
                .build();
        return schedule;
    }
}
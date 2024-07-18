package com.example.concert_reservation.domain.service.repository;

import com.example.concert_reservation.domain.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

    List<Schedule> findByConcertId(Integer concertId);

    Schedule save(Schedule schedule);

}

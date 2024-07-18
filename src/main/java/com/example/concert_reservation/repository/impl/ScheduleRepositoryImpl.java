package com.example.concert_reservation.repository.impl;

import com.example.concert_reservation.domain.entity.Schedule;
import com.example.concert_reservation.repository.ScheduleJpaRepository;
import com.example.concert_reservation.domain.service.repository.ScheduleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;

    public ScheduleRepositoryImpl(ScheduleJpaRepository scheduleJpaRepository) {
        this.scheduleJpaRepository = scheduleJpaRepository;
    }
    public List<Schedule> findByConcertId(Integer concertId) {
        return this.scheduleJpaRepository.findByConcertId(concertId);
    }

    public Schedule save(Schedule schedule) {
        return this.scheduleJpaRepository.save(schedule);
    }
}

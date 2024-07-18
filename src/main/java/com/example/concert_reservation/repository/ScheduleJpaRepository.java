package com.example.concert_reservation.repository;

import com.example.concert_reservation.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule, Integer> {

    List<Schedule> findByConcertId(Integer concertId);

}

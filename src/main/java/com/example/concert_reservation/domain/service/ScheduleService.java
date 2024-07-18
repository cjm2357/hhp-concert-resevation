package com.example.concert_reservation.domain.service;

import com.example.concert_reservation.domain.service.repository.ScheduleRepository;
import com.example.concert_reservation.domain.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getScheduleListByConcertId(Integer concertId) {
        return scheduleRepository.findByConcertId(concertId);
    }

}

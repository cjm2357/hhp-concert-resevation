package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Concert;
import com.example.concert_reservation.entity.Schedule;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.service.repository.ConcertRepository;
import com.example.concert_reservation.service.repository.ScheduleRepository;
import com.example.concert_reservation.service.repository.SeatRepository;
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

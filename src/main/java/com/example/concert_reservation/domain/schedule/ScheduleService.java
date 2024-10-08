package com.example.concert_reservation.domain.schedule;

import com.example.concert_reservation.domain.entity.Schedule;
import com.example.concert_reservation.infra.schedule.ScheduleCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleCacheRepository scheduleCacheRepository;

    public List<Schedule> getScheduleListByConcertId(Integer concertId) {
        return scheduleRepository.findByConcertId(concertId);
    }

}

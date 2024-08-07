package com.example.concert_reservation.repository.schedule;

import com.example.concert_reservation.domain.entity.Schedule;
import com.example.concert_reservation.domain.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;
    private final ScheduleCacheRepository scheduleCacheRepository;


    public List<Schedule> findByConcertId(Integer concertId) {
        List<Schedule> schedules = scheduleCacheRepository.findSchedulesByConcertId(concertId);
        if (schedules == null || schedules.size() == 0) {
            schedules = scheduleJpaRepository.findByConcertId(concertId);
            scheduleCacheRepository.saveSchedules(schedules);

        }
        return schedules;
    }

    public Schedule save(Schedule schedule) {
        return this.scheduleJpaRepository.save(schedule);
    }
}

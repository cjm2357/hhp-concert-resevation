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
    private final SeatRepository seatRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, SeatRepository seatRepository) {
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
    }

    public List<Schedule> getAvailableScheduleList(Integer concertId) {

        List<Schedule> schedules = scheduleRepository.findByConcertId(concertId);
        //concert 빈좌석 조회
        //빈좌석인 seat의 scheduleId와 schedule의 Id가 일치하는 schedule만 리턴한다.
        List<Seat> seats = seatRepository.findByConcertIdAndState(concertId, Seat.State.EMPTY);
        for (Schedule s : schedules) {
            System.out.println("AAA" + s.toString());
        }
        for (Seat s : seats) {
            System.out.println("SSS" + s.toString());
        }
        schedules = schedules.stream()
                .filter(schedule ->
                    seats.stream().filter(seat-> seat.getScheduleId() == schedule.getId()
                ).findAny().isPresent()
        ).toList();

        return schedules;
    }
}

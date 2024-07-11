package com.example.concert_reservation.service;


import com.example.concert_reservation.entity.Schedule;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.service.repository.ScheduleRepository;
import com.example.concert_reservation.service.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class ScheduleServiceIntegrationTest {


    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    SeatRepository seatRepository;

    @Test
    void 예약가능_스케쥴_조회_있음 () {
        //given
        Integer concertId = 1;

        Schedule schedule1 = new Schedule();
        schedule1.setConcertId(1);
        schedule1.setDate(LocalDateTime.of(2024,1,1,0,0));
        schedule1 = scheduleRepository.save(schedule1);

        Schedule schedule2 = new Schedule();
        schedule2.setConcertId(1);
        schedule2.setDate(LocalDateTime.of(2024,1,3,0,0));
        schedule2 = scheduleRepository.save(schedule2);

        Seat schedule1seat1 = new Seat();
        schedule1seat1.setConcertId(1);
        schedule1seat1.setScheduleId(schedule1.getId());
        schedule1seat1.setState(Seat.State.RESERVED);
        schedule1seat1 = seatRepository.save(schedule1seat1);

        Seat schedule1seat2 = new Seat();
        schedule1seat2.setConcertId(1);
        schedule1seat2.setScheduleId(schedule1.getId());
        schedule1seat2.setState(Seat.State.RESERVED);
        schedule1seat2= seatRepository.save(schedule1seat2);

        Seat schedule2seat1 = new Seat();
        schedule2seat1.setConcertId(1);
        schedule2seat1.setScheduleId(schedule2.getId());
        schedule2seat1.setState(Seat.State.RESERVED);
        schedule2seat1 = seatRepository.save(schedule2seat1);

        Seat schedule2seat2 = new Seat();
        schedule2seat2.setConcertId(1);
        schedule2seat2.setScheduleId(schedule2.getId());
        schedule2seat2.setState(Seat.State.EMPTY);
        schedule2seat2= seatRepository.save(schedule2seat2);



        //when
        List<Schedule> schedules = scheduleService.getAvailableScheduleList(concertId);

        //then
        assertEquals(1, schedules.size());
        assertEquals(2, schedules.get(0).getId());

    }


    @Test
    void 예약가능_스케쥴_조회_없음 () {
        //given
        Integer concertId = 1;

        //when
        List<Schedule> schedules = scheduleService.getAvailableScheduleList(concertId);

        //then
        assertEquals(0, schedules.size());

    }


}

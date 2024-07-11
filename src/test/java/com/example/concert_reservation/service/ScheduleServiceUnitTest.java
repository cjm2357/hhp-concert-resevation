package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Schedule;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.service.repository.ScheduleRepository;
import com.example.concert_reservation.service.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceUnitTest {
    
    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    SeatRepository seatRepository;

    @InjectMocks
    ScheduleService scheduleService;

    @Test
    void 예약가능한스케쥴_조회_성공() {
        //given
        Integer concertId = 1;

        Schedule schedule1 = new Schedule();
        schedule1.setId(1);
        schedule1.setConcertId(1);
        schedule1.setDate(LocalDateTime.of(2024,1,1,0,0));

        Schedule schedule2 = new Schedule();
        schedule2.setId(2);
        schedule2.setConcertId(1);
        schedule2.setDate(LocalDateTime.of(2024,1,3,0,0));
        List<Schedule> expectedSchedules = new ArrayList<>();
        expectedSchedules.add(schedule1);
        expectedSchedules.add(schedule2);


        //concert1, schedule1, seat [EMPTY, RESERVED]
        List<Seat> expectedSeats = new ArrayList<>();
        Seat seat1 = new Seat();
        seat1.setId(1);
        seat1.setConcertId(1);
        seat1.setScheduleId(1);
        seat1.setSeatNo(1);
        seat1.setState(Seat.State.EMPTY);
        Seat seat2 = new Seat();
        seat1.setId(2);
        seat1.setConcertId(1);
        seat1.setScheduleId(1);
        seat1.setSeatNo(2);
        seat1.setState(Seat.State.RESERVED);
        expectedSeats.add(seat1);
        expectedSeats.add(seat2);

        //concert1, schedule1, seat [RESERVED, RESERVED]
        Seat seat3 = new Seat();
        seat3.setId(3);
        seat3.setConcertId(1);
        seat3.setScheduleId(2);
        seat3.setSeatNo(1);
        seat3.setState(Seat.State.RESERVED);
        Seat seat4 = new Seat();
        seat3.setId(4);
        seat3.setConcertId(1);
        seat3.setScheduleId(2);
        seat3.setSeatNo(2);
        seat3.setState(Seat.State.RESERVED);
        expectedSeats.add(seat3);
        expectedSeats.add(seat4);


        when(seatRepository.findByConcertIdAndState(any(), any())).thenReturn(expectedSeats);
        when(scheduleRepository.findByConcertId(any())).thenReturn(expectedSchedules);

        //when
        List<Schedule> schedules = scheduleService.getAvailableScheduleList(concertId);

        //then
        assertEquals(expectedSchedules, schedules);
    }

    @Test
    void 예약가능일정_없음()  {
        //given
        List<Seat> expectedSeats = new ArrayList<>();

        Integer concertId = 1;

        Schedule schedule1 = new Schedule();
        schedule1.setId(1);
        schedule1.setConcertId(concertId);
        schedule1.setDate(LocalDateTime.of(2024,1,1,0,0));

        Schedule schedule2 = new Schedule();
        schedule2.setId(2);
        schedule2.setConcertId(concertId);
        schedule2.setDate(LocalDateTime.of(2024,1,3,0,0));
        List<Schedule> expectedSchedules = new ArrayList<>();
        expectedSchedules.add(schedule1);
        expectedSchedules.add(schedule2);

        when(seatRepository.findByConcertIdAndState(any(), any())).thenReturn(expectedSeats);
        when(scheduleRepository.findByConcertId(any())).thenReturn(expectedSchedules);

        //when
        List<Schedule> schedules = scheduleService.getAvailableScheduleList(concertId);

        //then
        assertEquals(0, schedules.size());
    }


}

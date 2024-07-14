package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.entity.Schedule;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.fixture.ScheduleFixture;
import com.example.concert_reservation.fixture.SeatFixture;
import com.example.concert_reservation.service.ScheduleService;
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

        Schedule schedule1 = ScheduleFixture.createSchedule(1, concertId, LocalDateTime.of(2024,1,1,0,0));
        Schedule schedule2 = ScheduleFixture.createSchedule(2, concertId, LocalDateTime.of(2024,1,3,0,0));

        List<Schedule> expectedSchedules = new ArrayList<>();
        expectedSchedules.add(schedule1);
        expectedSchedules.add(schedule2);

        //concert1, schedule1, seat [EMPTY, RESERVED]
        List<Seat> expectedSeats = new ArrayList<>();
        Seat seat1 = SeatFixture.createSeat(1, concertId, 1, 1, Seat.State.EMPTY, 1000l, "A");
        Seat seat2 = SeatFixture.createSeat(2, concertId, 1, 2, Seat.State.RESERVED, 1000l, "A");
        expectedSeats.add(seat1);
        expectedSeats.add(seat2);

        //concert1, schedule2, seat [RESERVED, RESERVED]
        Seat seat3 = SeatFixture.createSeat(3, concertId, 2, 1, Seat.State.RESERVED, 1000l, "A");
        Seat seat4 = SeatFixture.createSeat(4, concertId, 2, 2, Seat.State.RESERVED, 1000l, "A");
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

        Schedule schedule1 = ScheduleFixture.createSchedule(1, concertId, LocalDateTime.of(2024,1,1,0,0));
        Schedule schedule2 = ScheduleFixture.createSchedule(2, concertId, LocalDateTime.of(2024,1,3,0,0));
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

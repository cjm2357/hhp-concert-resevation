package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.domain.entity.Schedule;
import com.example.concert_reservation.fixture.ScheduleFixture;
import com.example.concert_reservation.domain.schedule.ScheduleService;
import com.example.concert_reservation.domain.schedule.ScheduleRepository;
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
    @InjectMocks
    ScheduleService scheduleService;

    @Test
    void 스케쥴리스트_조회_성공() {
        //given
        Integer concertId = 1;

        Schedule schedule1 = ScheduleFixture.createSchedule(1, concertId, LocalDateTime.of(2024,1,1,0,0));
        Schedule schedule2 = ScheduleFixture.createSchedule(2, concertId, LocalDateTime.of(2024,1,3,0,0));

        List<Schedule> expectedSchedules = new ArrayList<>();
        expectedSchedules.add(schedule1);
        expectedSchedules.add(schedule2);

        when(scheduleRepository.findByConcertId(any())).thenReturn(expectedSchedules);

        //when
        List<Schedule> schedules = scheduleService.getScheduleListByConcertId(concertId);

        //then
        assertEquals(expectedSchedules, schedules);
    }

    @Test
    void 예약가능일정_없음()  {
        //given
        Integer concertId = 1;

        when(scheduleRepository.findByConcertId(any())).thenReturn(new ArrayList<>());

        //when
        List<Schedule> schedules = scheduleService.getScheduleListByConcertId(concertId);

        //then
        assertEquals(0, schedules.size());
    }


}

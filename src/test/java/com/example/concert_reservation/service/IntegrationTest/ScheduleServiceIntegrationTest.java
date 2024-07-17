//package com.example.concert_reservation.service.IntegrationTest;
//
//
//import com.example.concert_reservation.entity.Schedule;
//import com.example.concert_reservation.entity.Seat;
//import com.example.concert_reservation.fixture.ScheduleFixture;
//import com.example.concert_reservation.fixture.SeatFixture;
//import com.example.concert_reservation.service.ScheduleService;
//import com.example.concert_reservation.service.repository.ScheduleRepository;
//import com.example.concert_reservation.service.repository.SeatRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//@SpringBootTest
//@Transactional
//public class ScheduleServiceIntegrationTest {
//
//
//    @Autowired
//    ScheduleService scheduleService;
//
//    @Autowired
//    ScheduleRepository scheduleRepository;
//
//    @Autowired
//    SeatRepository seatRepository;
//
//    @Test
//    void 예약가능_스케쥴_조회_있음 () {
//        //given
//        Integer concertId = 1;
//
//        Schedule schedule1 = ScheduleFixture.createSchedule(null, concertId, LocalDateTime.of(2024,1,1,0,0 ));
//        schedule1 = scheduleRepository.save(schedule1);
//
//        Schedule schedule2 = ScheduleFixture.createSchedule(null, concertId, LocalDateTime.of(2024,1,3,0,0 ));
//        schedule2 = scheduleRepository.save(schedule2);
//
//        Seat schedule1seat1 = SeatFixture.createSeat(null, concertId, schedule1.getId(), 1, Seat.State.RESERVED, 1000l, "A");
//        schedule1seat1 = seatRepository.save(schedule1seat1);
//
//        Seat schedule1seat2 = SeatFixture.createSeat(null, concertId, schedule1.getId(), 2, Seat.State.RESERVED, 1000l, "A");
//        schedule1seat2= seatRepository.save(schedule1seat2);
//
//        Seat schedule2seat1 = SeatFixture.createSeat(null, concertId, schedule2.getId(), 1, Seat.State.RESERVED, 1000l, "A");
//        schedule2seat1 = seatRepository.save(schedule2seat1);
//
//        Seat schedule2seat2 = SeatFixture.createSeat(null, concertId, schedule2.getId(), 2, Seat.State.EMPTY, 1000l, "A");
//        schedule2seat2= seatRepository.save(schedule2seat2);
//
//
//        //when
//        List<Schedule> schedules = scheduleService.getAvailableScheduleList(concertId);
//
//        //then
//        assertEquals(1, schedules.size());
//        assertEquals(2, schedules.get(0).getId());
//
//    }
//
//
//    @Test
//    void 예약가능_스케쥴_조회_없음 () {
//        //given
//        Integer concertId = 1;
//
//        //when
//        List<Schedule> schedules = scheduleService.getAvailableScheduleList(concertId);
//
//        //then
//        assertEquals(0, schedules.size());
//
//    }
//
//
//}

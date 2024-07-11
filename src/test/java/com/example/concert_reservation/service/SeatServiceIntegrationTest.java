package com.example.concert_reservation.service;


import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.repository.ReservationRepository;
import com.example.concert_reservation.service.repository.SeatRepository;
import com.example.concert_reservation.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
public class SeatServiceIntegrationTest {


    @Autowired
    SeatService seatService;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        User user = new User();
        user.setId(1);
        user.setName("유저1");
        userRepository.save(user);
    }

    @Test
    void 예약가능_좌석_조회_있음 () {
        //given
        Integer scheduleId = 1;

        Seat seat1 = new Seat();
        seat1.setScheduleId(scheduleId);
        seat1.setState(Seat.State.RESERVED);
        seat1.setSeatNo(1);
        seat1 = seatRepository.save(seat1);

        Seat seat2 = new Seat();
        seat2.setScheduleId(scheduleId);
        seat2.setState(Seat.State.EMPTY);
        seat2.setSeatNo(2);
        seat2 = seatRepository.save(seat2);

        Seat seat3 = new Seat();
        seat3.setScheduleId(scheduleId);
        seat3.setState(Seat.State.EMPTY);
        seat3.setSeatNo(3);
        seat3 = seatRepository.save(seat3);


        //when
        List<Seat> seats = seatService.getAvailableSeatList(scheduleId);

        //then
        assertEquals(2, seats.size());
        assertEquals(seat2.getSeatNo(), seats.get(0).getSeatNo());
        assertEquals(seat3.getSeatNo(), seats.get(1).getSeatNo());

    }

    @Test
    void 예약가능_좌석_조회_없음 () {
        //given
        Integer scheduleId = 1;


        //when
        List<Seat> seats = seatService.getAvailableSeatList(scheduleId);

        //then
        assertEquals(0, seats.size());

    }
    
    @Test
    void 좌석_예약_성공() {

        //given
        Integer scheduleId = 1;
        Integer userId = 1;
        Integer concertId = 1;


        Seat seat1 = new Seat();
        seat1.setScheduleId(scheduleId);
        seat1.setState(Seat.State.RESERVED);
        seat1.setSeatNo(1);
        seat1.setConcertId(concertId);
        seat1.setGrade("A");
        seat1.setPrice(1000l);

        seat1 = seatRepository.save(seat1);

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setSeatId(seat1.getId());

        //when
        reservation = seatService.reserveSeat(reservation);

        //then
        Seat seat = seatRepository.findById(seat1.getId());
        assertEquals(1, reservation.getConcertId());
        assertEquals("A", reservation.getSeatGrade());
        assertEquals(1000l, reservation.getPrice());
        assertEquals(Reservation.State.WAITING, reservation.getState());
        assertEquals(Seat.State.RESERVED, seat.getState());
    }

    @Test
    void 좌석_예약_실패() {

        //given
        Integer scheduleId = 1;
        Integer userId = 1;
        Integer concertId = 1;


        Seat seat1 = new Seat();
        seat1.setScheduleId(scheduleId);
        seat1.setState(Seat.State.RESERVED);
        seat1.setSeatNo(1);
        seat1.setConcertId(concertId);
        seat1.setGrade("A");
        seat1.setPrice(1000l);

        seat1 = seatRepository.save(seat1);

        Reservation prevReservation = new Reservation();
        prevReservation.setUserId(2);
        prevReservation.setSeatId(seat1.getId());

        seatService.reserveSeat(prevReservation);

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setSeatId(seat1.getId());


        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            seatService.reserveSeat(reservation);
        });

        //then
        assertEquals("이미 예약된 정보가 있습니다.", exception.getMessage().toString());

    }

    @Test
    void 예약만료_좌석_상태_변경 () {
        //given
        Integer scheduleId = 1;
        Integer concertId = 1;

        Seat seat1 = new Seat();
        seat1.setScheduleId(scheduleId);
        seat1.setState(Seat.State.RESERVED);
        seat1.setSeatNo(1);
        seat1.setConcertId(concertId);
        seat1.setGrade("A");
        seat1.setPrice(1000l);
        seat1 = seatRepository.save(seat1);

        Seat seat2 = new Seat();
        seat2.setScheduleId(scheduleId);
        seat2.setState(Seat.State.RESERVED);
        seat2.setSeatNo(2);
        seat2.setConcertId(concertId);
        seat2.setGrade("A");
        seat2.setPrice(1000l);
        seat2 = seatRepository.save(seat2);

        Reservation reservation1 = new Reservation();
        reservation1.setUserId(1);
        reservation1.setSeatId(seat1.getId());
        reservation1.setSeatGrade(seat1.getGrade());
        reservation1.setState(Reservation.State.WAITING);
        reservation1.setConcertId(seat1.getConcertId());
        reservation1.setPrice(seat1.getPrice());
        reservation1.setScheduleId(seat1.getScheduleId());
        reservation1.setSeatNo(seat1.getSeatNo());
        reservation1.setCreatedTime(LocalDateTime.now().minusMinutes(15));
        reservation1.setExpiredTime(LocalDateTime.now().minusMinutes(5));
        reservation1 = reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setUserId(2);
        reservation2.setSeatId(seat2.getId());
        reservation2.setSeatGrade(seat2.getGrade());
        reservation2.setState(Reservation.State.WAITING);
        reservation2.setConcertId(seat2.getConcertId());
        reservation2.setPrice(seat2.getPrice());
        reservation2.setScheduleId(seat2.getScheduleId());
        reservation2.setSeatNo(seat2.getSeatNo());
        reservation2.setCreatedTime(LocalDateTime.now().minusMinutes(11));
        reservation2.setExpiredTime(LocalDateTime.now().minusMinutes(1));
        reservation2 = reservationRepository.save(reservation2);


        //when
        seatService.expireReservation();

        //then
        reservation1 = reservationRepository.findById(reservation1.getId());
        reservation2 = reservationRepository.findById(reservation2.getId());
        seat1 = seatRepository.findById(reservation1.getSeatId());
        seat2 = seatRepository.findById(reservation2.getSeatId());
        assertEquals(Reservation.State.EXPIRED, reservation1.getState());
        assertEquals(Reservation.State.EXPIRED, reservation2.getState());
        assertEquals(Seat.State.EMPTY, seat1.getState());
        assertEquals(Seat.State.EMPTY, seat2.getState());


    }



}

package com.example.concert_reservation.facade.integrationTest;

import com.example.concert_reservation.application.ConcertFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.*;
import com.example.concert_reservation.domain.service.repository.*;
import com.example.concert_reservation.fixture.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
public class ConcertFacadeIntegrationTest {

    @Autowired
    ConcertFacade concertFacade;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointRepository pointRepository;

    @BeforeEach
    void init() {
        User user = UserFixture.createUser(1, "유저1", 1, 10000l);
        pointRepository.save(user.getPoint());
        userRepository.save(user);
    }

    @Test
    void 콘서트조회_성공 () {
        //given
        Concert concert1 = ConcertFixture.createConcert(null,"A concert");
        Concert concert2 = ConcertFixture.createConcert(null,"B concert");
        concertRepository.save(concert1);
        concertRepository.save(concert2);

        //when
        List<Concert> concerts = concertFacade.getConcertList();

        //then
        assertEquals(2, concerts.size());
    }

    @Test
    void 콘서트조회_결과_없음 () {

        //when
        List<Concert> concerts = concertFacade.getConcertList();

        //then
        assertEquals(0, concerts.size());
    }

    @Test
    void 예약가능_스케쥴_조회_있음 () {
        //given
        Integer concertId = 1;

        Schedule schedule1 = ScheduleFixture.createSchedule(null, concertId, LocalDateTime.of(2024, 1, 1, 0, 0));
        schedule1 = scheduleRepository.save(schedule1);

        Schedule schedule2 = ScheduleFixture.createSchedule(null, concertId, LocalDateTime.of(2024, 1, 3, 0, 0));
        schedule2 = scheduleRepository.save(schedule2);

        Seat schedule1seat1 = SeatFixture.createSeat(null, concertId, schedule1.getId(), 1, Seat.State.RESERVED, 1000l, "A");
        seatRepository.save(schedule1seat1);

        Seat schedule1seat2 = SeatFixture.createSeat(null, concertId, schedule1.getId(), 2, Seat.State.RESERVED, 1000l, "A");
        seatRepository.save(schedule1seat2);

        Seat schedule2seat1 = SeatFixture.createSeat(null, concertId, schedule2.getId(), 1, Seat.State.RESERVED, 1000l, "A");
        seatRepository.save(schedule2seat1);

        Seat schedule2seat2 = SeatFixture.createSeat(null, concertId, schedule2.getId(), 2, Seat.State.EMPTY, 1000l, "A");
        seatRepository.save(schedule2seat2);

        //when
        List<Schedule> schedules = concertFacade.getAvailableScheduleList(concertId);

        //then
        assertEquals(1, schedules.size());
        assertEquals(2, schedules.get(0).getId());
    }

    @Test
    void 예약가능_스케쥴_조회_없음 () {
        //given
        Integer concertId = 1;

        //when
        List<Schedule> schedules = concertFacade.getAvailableScheduleList(concertId);

        //then
        assertEquals(0, schedules.size());

    }

    @Test
    void 예약가능_좌석_조회_있음 () {
        //given
        Integer scheduleId = 1;

        Seat seat1 = SeatFixture.createSeat(null, 1, scheduleId, 1, Seat.State.RESERVED, 10000l, "A");
        seat1 = seatRepository.save(seat1);

        Seat seat2 = SeatFixture.createSeat(null, 1, scheduleId, 2, Seat.State.EMPTY, 10000l, "A");
        seat2 = seatRepository.save(seat2);

        Seat seat3 = SeatFixture.createSeat(null, 1, scheduleId, 3, Seat.State.EMPTY, 10000l, "A");
        seat3 = seatRepository.save(seat3);


        //when
        List<Seat> seats = concertFacade.getAvailableSeatList(scheduleId);

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
        List<Seat> seats = concertFacade.getAvailableSeatList(scheduleId);

        //then
        assertEquals(0, seats.size());

    }

    @Test
    void 좌석_예약_성공() {

        //given
        Integer scheduleId = 1;
        Integer userId = 1;
        Integer concertId = 1;

        Seat seat1 = SeatFixture.createSeat(null, concertId, scheduleId, 1, Seat.State.RESERVED, 1000l, "A");
        seat1 = seatRepository.save(seat1);

        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setSeatId(seat1.getId());

        //when
        reservation = concertFacade.reserveSeat(seat1.getId(), userId);

        //then
        Seat seat = seatRepository.findById(seat1.getId());
        assertEquals(1, reservation.getConcertId());
        assertEquals("A", reservation.getSeatGrade());
        assertEquals(1000l, reservation.getPrice());
        assertEquals(Reservation.State.WAITING, reservation.getState());
        assertEquals(Seat.State.RESERVED, seat.getState());
    }
//
    @Test
    void 좌석_예약_실패_다른인원선점() {
        //given
        Integer scheduleId = 1;
        Integer userId = 1;
        Integer userId2 = 2;
        Integer seatId = 1;
        Integer concertId = 1;

        Seat seat1 =  SeatFixture.createSeat(seatId, concertId, scheduleId, 1, Seat.State.EMPTY, 1000l, "A");
        seat1 = seatRepository.save(seat1);

        concertFacade.reserveSeat(seat1.getId(), userId);

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.reserveSeat(seatId, userId2);
        });

        //then
        assertEquals(CustomExceptionCode.RESERVATION_EXIST.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.RESERVATION_EXIST.getMessage(), exception.getCustomExceptionCode().getMessage());

    }

    @Test
    void 예약만료_좌석_상태_변경 () {
        //given
        Integer scheduleId = 1;
        Integer concertId = 1;

        Seat seat1 =  SeatFixture.createSeat(null, concertId, scheduleId, 1, Seat.State.RESERVED, 1000l, "A");
        seat1 = seatRepository.save(seat1);

        Seat seat2 =  SeatFixture.createSeat(null, concertId, scheduleId, 2, Seat.State.RESERVED, 1000l, "A");
        seat2 = seatRepository.save(seat2);

        Reservation reservation1 =
                ReservationFixture.creasteReservation(null, 1, seat1.getConcertId(), seat1.getId(), seat1.getScheduleId(), seat1.getSeatNo(), Reservation.State.WAITING, seat1.getPrice(), seat1.getGrade(), LocalDateTime.now().minusMinutes(15));
        reservation1 = reservationRepository.save(reservation1);

        Reservation reservation2 =
                ReservationFixture.creasteReservation(null, 2, seat2.getConcertId(), seat2.getId(), seat2.getScheduleId(), seat2.getSeatNo(), Reservation.State.WAITING, seat2.getPrice(), seat2.getGrade(), LocalDateTime.now().minusMinutes(11));
        reservation2 = reservationRepository.save(reservation2);

        //when
        concertFacade.expireReservation();

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

    @Test
    void 결제_성공() {
        //given
        Integer userId = 1;

        Reservation reservation =
                ReservationFixture.creasteReservation(null, userId, 1, 1, 1, 1, Reservation.State.WAITING, 8000l, "A", LocalDateTime.now());
        reservation = reservationRepository.save(reservation);

        Payment payment = PaymentFixture.createPayment(null, userId, reservation.getId(), LocalDateTime.now());

        //when
        payment = concertFacade.pay(payment);

        //then
        User user = userRepository.findById(userId);
        reservation = reservationRepository.findById(reservation.getId());
        assertNotNull(payment.getId());
        assertEquals(10000-8000, user.getPoint().getAmount());
        assertEquals(Reservation.State.COMPLETED, reservation.getState());
    }

    @Test
    void 예약정보없음() {
        //given
        Integer userId = 1;

        Payment payment =  PaymentFixture.createPayment(null, userId, 1, LocalDateTime.now());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.pay(payment);
        });

        //then
        User user = userRepository.findById(userId);
        assertEquals(CustomExceptionCode.RESERVATION_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.RESERVATION_NOT_FOUND.getMessage(), exception.getCustomExceptionCode().getMessage().toString());
        assertEquals(10000, user.getPoint().getAmount());

    }


    @Test
    void 결제시간만료() {
        //given
        Integer userId = 1;

        Reservation reservation =
                ReservationFixture.creasteReservation(null, userId,1, 1, 1, 1, Reservation.State.EXPIRED, 8000l, "A", LocalDateTime.now().minusMinutes(10));
        reservation = reservationRepository.save(reservation);

        Payment payment = PaymentFixture.createPayment(null, userId, reservation.getId(), LocalDateTime.now());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.pay(payment);
        });

        //then
        User user = userRepository.findById(userId);
        assertEquals(CustomExceptionCode.PAYMENT_TIME_EXPIRE.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.PAYMENT_TIME_EXPIRE.getMessage(), exception.getCustomExceptionCode().getMessage().toString());
        assertEquals(10000, user.getPoint().getAmount());

    }


    @Test
    void 잔액부족() {
        //given
        Integer userId = 1;

        Reservation reservation = ReservationFixture.creasteReservation(null, userId,1, 1, 1, 1, Reservation.State.WAITING, 12000l, "A", LocalDateTime.now());

        reservation = reservationRepository.save(reservation);

        Payment payment = PaymentFixture.createPayment(null, userId, reservation.getId(), LocalDateTime.now());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.pay(payment);
        });

        //then
        User user = userRepository.findById(userId);
        reservation = reservationRepository.findById(reservation.getId());
        assertEquals(CustomExceptionCode.POINT_NOT_ENOUGH.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.POINT_NOT_ENOUGH.getMessage(), exception.getCustomExceptionCode().getMessage().toString());
        assertEquals(10000, user.getPoint().getAmount());
        assertEquals(Reservation.State.WAITING, reservation.getState());
    }


}

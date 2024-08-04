package com.example.concert_reservation.facade.unitTest;


import com.example.concert_reservation.application.ConcertFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.*;
import com.example.concert_reservation.domain.service.*;
import com.example.concert_reservation.fixture.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertFacadeUnitTest {

    @Mock
    ConcertService concertService;

    @Mock
    ScheduleService scheduleService;

    @Mock
    SeatService seatService;

    @Mock
    ReservationService reservationService;

    @Mock
    PaymentService paymentService;

    @Mock
    UserService userService;

    @Mock
    PointService pointService;

    @Mock
    TokenService tokenService;

    @InjectMocks
    ConcertFacade concertFacade;

    @Test
    void 콘서트리스트_조회_성공() {
        //given
        List<Concert> expectedConcertList = new ArrayList<>();
        Concert concert1 = ConcertFixture.createConcert(1, "아이유 콘서트");
        Concert concert2 = ConcertFixture.createConcert(2, "박효신 콘서트");
        expectedConcertList.add(concert1);
        expectedConcertList.add(concert2);

        when(concertFacade.getConcertList()).thenReturn(expectedConcertList);

        //when
        List<Concert> concertList = concertService.getConcertList();

        //then
        assertEquals(concertList, expectedConcertList);
    }

    @Test
    void 콘서트리스트_조회_결과_없음() {
        //given
        List<Concert> expectedConcertList = new ArrayList<>();


        when(concertFacade.getConcertList()).thenReturn(expectedConcertList);

        //when
        List<Concert> concertList = concertService.getConcertList();

        //then
        assertEquals(0, concertList.size());
    }

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

        when(scheduleService.getScheduleListByConcertId(any())).thenReturn(expectedSchedules);
        when(seatService.getAvailableSeatListByConcertId(any())).thenReturn(expectedSeats);

        //when
        List<Schedule> schedules = concertFacade.getAvailableScheduleList(concertId);

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

        when(scheduleService.getScheduleListByConcertId(any())).thenReturn(expectedSchedules);
        when(seatService.getAvailableSeatListByConcertId(any())).thenReturn(expectedSeats);

        //when
        List<Schedule> schedules = concertFacade.getAvailableScheduleList(concertId);

        //then
        assertEquals(0, schedules.size());
    }

    @Test
    void 예약가능한시트_조회_성공() {
        //given
        Integer scheduleId = 1;

        List<Seat> expectedSeats = new ArrayList<>();
        Seat seat1 = SeatFixture.createSeat(1, 1, scheduleId, 1, Seat.State.EMPTY, 1000l, "A");
        Seat seat2 = SeatFixture.createSeat(2, 1, scheduleId, 2, Seat.State.EMPTY, 1000l, "A");
        expectedSeats.add(seat1);
        expectedSeats.add(seat2);

        when(seatService.getAvailableSeatList(any())).thenReturn(expectedSeats);

        //when
        List<Seat> seats = concertFacade.getAvailableSeatList(scheduleId);

        //then
        assertEquals(expectedSeats, seats);
    }

    @Test
    void 예약가능한시트_조회_없음() {
        //given
        Integer scheduleId = 1;

        List<Seat> expectedSeats = new ArrayList<>();

        when(seatService.getAvailableSeatList(any())).thenReturn(expectedSeats);

        //when
        List<Seat> seats = concertFacade.getAvailableSeatList(scheduleId);

        //then
        assertEquals(expectedSeats, seats);
        assertEquals(0, seats.size());
    }

    @Test
    void 좌석예약_성공()  {
        //given
        Integer seatId = 1;
        Integer userId = 1;

        Seat expectedSeat = SeatFixture.createSeat(seatId, 1, 1, 1, Seat.State.EMPTY, 1000l, "A");

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, userId,1, seatId, 1, 1, Reservation.State.WAITING, expectedSeat.getPrice(), expectedSeat.getGrade(), LocalDateTime.now());


//        when(reservationRepository.findBySeatIdWithLock(any())).thenReturn(null);
        when(seatService.updateSeatState(any(), any())).thenReturn(expectedSeat);
        when(reservationService.reserveSeat(any())).thenReturn(expectedReservation);

        //when
        Reservation reservation = concertFacade.reserveSeat(seatId, userId);


        //then
        assertEquals(expectedReservation, reservation);
    }

    @Test
    void 좌석예약_실패()  {
        //given
        Integer seatId = 1;
        Integer userId = 1;

        Seat seatInfo = SeatFixture.createSeat(1, 1, 1, 1, Seat.State.RESERVED, 10000l, "A");


        when(seatService.updateSeatState(any(), any())).thenReturn(seatInfo);
        when(reservationService.reserveSeat(any())).thenThrow(new CustomException(CustomExceptionCode.RESERVATION_FAILED));
        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.reserveSeat(seatId, userId);
        });
        //then
        assertEquals(CustomExceptionCode.RESERVATION_FAILED.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.RESERVATION_FAILED.getMessage(), exception.getCustomExceptionCode().getMessage());

    }

    @Test
    void 결제_성공 () {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1, 1, 1, Reservation.State.WAITING, 10000l, "A", LocalDateTime.now().minusMinutes(3));

        User expectedUser = UserFixture.createUser(1, "user1", 1, 20000l);

        Payment expectedPayment = PaymentFixture.createPayment(1, 1, 1, LocalDateTime.now());

        Point expectedPoint = new Point();
        expectedPoint.setId(1);
        expectedPoint.setUserId(1);
        expectedPoint.setAmount(expectedPoint.getAmount() - expectedReservation.getPrice());

        UUID tokenKey = UUID.randomUUID();

        when(reservationService.getReservation(any())).thenReturn(expectedReservation);
        when(userService.getUser(any())).thenReturn(expectedUser);
        when(pointService.savePoint(any(), any())).thenReturn(expectedPoint);
        when(paymentService.pay(any())).thenReturn(expectedPayment);

        //when
        Payment payment = concertFacade.pay(requestPayment, tokenKey);

        //then
        assertEquals(1, payment.getId());
        assertEquals(1, payment.getReservationId());
        assertEquals(1, payment.getUserId());

    }

    @Test
    void 예약_정보_없음 () {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);
        UUID tokenKey = UUID.randomUUID();

        when(reservationService.getReservation(any())).thenThrow(new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND));

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.pay(requestPayment,tokenKey);
        });
        //then
        assertEquals(CustomExceptionCode.RESERVATION_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.RESERVATION_NOT_FOUND.getMessage(), exception.getCustomExceptionCode().getMessage());

    }

    @Test
    void 결제_시간_만료() {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        UUID tokenKey = UUID.randomUUID();

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1, 1, 1, Reservation.State.EXPIRED, 10000l, "A", LocalDateTime.now().minusMinutes(8));

        when(reservationService.getReservation(any())).thenReturn(expectedReservation);

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.pay(requestPayment,tokenKey);
        });
        //then
        assertEquals(CustomExceptionCode.PAYMENT_TIME_EXPIRE.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.PAYMENT_TIME_EXPIRE.getMessage(), exception.getCustomExceptionCode().getMessage());

    }


    @Test
    void 결제실패_유저의_포인트정보_없음() {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        UUID tokenKey = UUID.randomUUID();

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1, 1, 1, Reservation.State.WAITING, 10000l, "A", LocalDateTime.now().minusMinutes(3));


        User expectUser = new User();
        expectUser.setId(1);

        when(reservationService.getReservation(any())).thenReturn(expectedReservation);
        when(userService.getUser(any())).thenReturn(expectUser);


        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.pay(requestPayment, tokenKey);
        });
        //then
        assertEquals(CustomExceptionCode.USER_POINT_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.USER_POINT_NOT_FOUND.getMessage(), exception.getCustomExceptionCode().getMessage());

    }

    @Test
    void 결제실패_유저의_포인트_부족() {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        UUID tokenKey = UUID.randomUUID();

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1, 1, 1, Reservation.State.WAITING, 10000l, "A", LocalDateTime.now().minusMinutes(3));


        User expectUser = UserFixture.createUser(1, "user1", 1, 1000l);

        when(reservationService.getReservation(any())).thenReturn(expectedReservation);
        when( userService.getUser(any())).thenReturn(expectUser);


        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            concertFacade.pay(requestPayment, tokenKey);
        });
        //then
        assertEquals(CustomExceptionCode.POINT_NOT_ENOUGH.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.POINT_NOT_ENOUGH.getMessage(), exception.getCustomExceptionCode().getMessage());
    }
}

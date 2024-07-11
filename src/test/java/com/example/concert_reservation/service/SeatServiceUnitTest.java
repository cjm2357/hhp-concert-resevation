package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.service.repository.ReservationRepository;
import com.example.concert_reservation.service.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatServiceUnitTest {

    @Mock
    SeatRepository seatRepository;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    SeatService seatService;

    @Test
    void 예약가능한시트_조회_성공() {
        //given
        Integer scheduleId = 1;

        List<Seat> expectedSeats = new ArrayList<>();
        Seat seat1 = new Seat();
        seat1.setId(1);
        seat1.setConcertId(1);
        seat1.setScheduleId(scheduleId);
        seat1.setSeatNo(1);
        seat1.setState(Seat.State.EMPTY);

        Seat seat2 = new Seat();
        seat2.setId(2);
        seat2.setConcertId(1);
        seat2.setScheduleId(scheduleId);
        seat2.setSeatNo(2);
        seat2.setState(Seat.State.EMPTY);

        expectedSeats.add(seat1);
        expectedSeats.add(seat2);

        when(seatRepository.findByScheduleIdAndState(any(), any())).thenReturn(expectedSeats);

        //when
        List<Seat> seats = seatService.getAvailableSeatList(scheduleId);

        //then
        assertEquals(expectedSeats, seats);
    }

    @Test
    void 예약가능한시트_조회_실패() {
        //given
        Integer scheduleId = 1;

        List<Seat> expectedSeats = new ArrayList<>();

        when(seatRepository.findByScheduleIdAndState(any(), any())).thenReturn(expectedSeats);

        //when
        List<Seat> seats = seatService.getAvailableSeatList(scheduleId);

        //then
        assertEquals(expectedSeats, seats);
        assertEquals(0, seats.size());
    }

    @Test
    void 좌석예약_성공()  {
        //given
        Integer seatId = 1;
        Integer userId = 1;
        Reservation requestReservation = new Reservation();
        requestReservation.setSeatId(seatId);
        requestReservation.setUserId(userId);

        Seat expectedSeat = new Seat();
        expectedSeat.setState(Seat.State.EMPTY);
        expectedSeat.setId(seatId);
        expectedSeat.setScheduleId(1);
        expectedSeat.setPrice(1000l);
        expectedSeat.setSeatNo(1);
        expectedSeat.setConcertId(1);

        Reservation expectedReservation = new Reservation();
        expectedReservation.setId(1);
        expectedReservation.setUserId(userId);
        expectedReservation.setSeatNo(expectedSeat.getSeatNo());
        expectedReservation.setSeatId(seatId);
        expectedReservation.setState(Reservation.State.WAITING);

        when(reservationRepository.findBySeatIdWithLock(any())).thenReturn(null);
        when(seatRepository.findById(any())).thenReturn(expectedSeat);
        when(reservationRepository.save(any())).thenReturn(expectedReservation);

        //when
        Reservation reservation = seatService.reserveSeat(requestReservation);


        //then
        assertEquals(expectedReservation, reservation);
    }

    @Test
    void 좌석예약_실패()  {
        //given
        Integer seatId = 1;
        Integer userId = 1;
        Reservation requestReservation = new Reservation();
        requestReservation.setSeatId(seatId);
        requestReservation.setUserId(userId);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setUserId(userId);
        reservation.setSeatNo(1);
        reservation.setSeatId(seatId);
        reservation.setState(Reservation.State.WAITING);

        when(reservationRepository.findBySeatIdWithLock(any())).thenReturn(reservation);
        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            seatService.reserveSeat(requestReservation);
        });
        //then
        assertEquals("이미 예약된 정보가 있습니다.", exception.getMessage().toString());
    }
}

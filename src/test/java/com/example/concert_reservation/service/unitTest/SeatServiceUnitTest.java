package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Seat;
import com.example.concert_reservation.fixture.SeatFixture;
import com.example.concert_reservation.domain.service.SeatService;
import com.example.concert_reservation.domain.service.repository.SeatRepository;
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

    @InjectMocks
    SeatService seatService;

    @Test
    void 시트ID로_시트_조회_성공() {
        //given
        Integer seatId = 1;
        Seat expectedSeat = SeatFixture.createSeat(seatId, 1, seatId,1 ,Seat.State.EMPTY  ,10000l, "A");
        when(seatRepository.findById(any())).thenReturn(expectedSeat);

        //when
        Seat seat = seatService.getSeatById(seatId);

        //then
        assertEquals(expectedSeat, seat);
    }

    @Test
    void 시트ID로_시트_조회_실패_시트없음() {
        //given
        Integer seatId = 1;

        when(seatRepository.findById(any())).thenReturn(null);

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            seatService.getSeatById(seatId);
        });

        //then
        assertEquals(CustomExceptionCode.SEAT_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.SEAT_NOT_FOUND.getMessage(), exception.getCustomExceptionCode().getMessage());
    }

    @Test
    void 스케쥴ID로_예약가능한시트_조회_성공() {
        //given
        Integer scheduleId = 1;

        List<Seat> expectedSeats = new ArrayList<>();
        Seat seat1 = SeatFixture.createSeat(1, 1, scheduleId, 1, Seat.State.EMPTY, 1000l, "A");
        Seat seat2 = SeatFixture.createSeat(2, 1, scheduleId, 2, Seat.State.EMPTY, 1000l, "A");
        expectedSeats.add(seat1);
        expectedSeats.add(seat2);

        when(seatRepository.findByScheduleIdAndState(any(), any())).thenReturn(expectedSeats);

        //when
        List<Seat> seats = seatService.getAvailableSeatList(scheduleId);

        //then
        assertEquals(2, seats.size());
        assertEquals(Seat.State.EMPTY, seats.get(0).getState());
        assertEquals(Seat.State.EMPTY, seats.get(1).getState());
    }

    @Test
    void 스케쥴ID로_예약가능한시트_조회_실패() {
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
    void 콘서트ID로_예약가능한_시트리스트_조회_성공 () {
        //given
        Integer concertId = 1;

        List<Seat> expectedSeats = new ArrayList<>();
        Seat seat1 = SeatFixture.createSeat(1, concertId, 1, 1, Seat.State.EMPTY, 1000l, "A");
        Seat seat2 = SeatFixture.createSeat(2, concertId, 1, 2, Seat.State.EMPTY, 1000l, "A");
        expectedSeats.add(seat1);
        expectedSeats.add(seat2);

        when(seatRepository.findByConcertIdAndState(any(), any())).thenReturn(expectedSeats);

        //when
        List<Seat> seats = seatService.getAvailableSeatListByConcertId(concertId);

        //then
        assertEquals(2, seats.size());
        assertEquals(Seat.State.EMPTY, seats.get(0).getState());
        assertEquals(Seat.State.EMPTY, seats.get(1).getState());
    }

    @Test
    void 콘서트ID로_예약가능한_시트리스트_조회_실패 () {
        //given
        Integer concertId = 1;


        when(seatRepository.findByConcertIdAndState(any(), any())).thenReturn(new ArrayList<>());

        //when
        List<Seat> seats = seatService.getAvailableSeatListByConcertId(concertId);

        //then
        assertEquals(0, seats.size());
    }


}

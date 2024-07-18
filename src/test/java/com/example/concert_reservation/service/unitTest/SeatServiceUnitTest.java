package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.fixture.ReservationFixture;
import com.example.concert_reservation.fixture.SeatFixture;
import com.example.concert_reservation.service.SeatService;
import com.example.concert_reservation.service.repository.ReservationRepository;
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
        Seat seat1 = SeatFixture.createSeat(1, 1, scheduleId, 1, Seat.State.EMPTY, 1000l, "A");
        Seat seat2 = SeatFixture.createSeat(2, 1, scheduleId, 2, Seat.State.EMPTY, 1000l, "A");
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

        Seat expectedSeat = SeatFixture.createSeat(seatId, 1, 1, 1, Seat.State.EMPTY, 1000l, "A");

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, userId,1, seatId, 1, 1, Reservation.State.WAITING, expectedSeat.getPrice(), expectedSeat.getGrade(), LocalDateTime.now());


//        when(reservationRepository.findBySeatIdWithLock(any())).thenReturn(null);
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

        Reservation reservation =
                ReservationFixture.creasteReservation(1, userId,1, seatId, 1, 1, Reservation.State.EXPIRED, 1000l, "A", LocalDateTime.now());

        List<Reservation> expectedList = new ArrayList<>();
        expectedList.add(reservation);

        when(reservationRepository.findReservedReservationBySeatId(any())).thenReturn(expectedList);
        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            seatService.reserveSeat(requestReservation);
        });
        //then
        assertEquals("이미 예약된 정보가 있습니다.", exception.getMessage().toString());
    }
}
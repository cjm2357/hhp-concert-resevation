package com.example.concert_reservation.service.unitTest;


import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.fixture.ReservationFixture;
import com.example.concert_reservation.domain.reservation.ReservationService;
import com.example.concert_reservation.domain.reservation.ReservationRepository;
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
public class ReservationServiceUnitTest {

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

    @Test
    void 예약_조회_성공 () {
        //given
        Integer reservationId = 1;
        Reservation expectedReservation =
                ReservationFixture.creasteReservation(reservationId, 1, 1, 1, 1, 1, Reservation.State.WAITING, 10000l, "A" , LocalDateTime.now());
        when(reservationRepository.findById(any())).thenReturn(expectedReservation);
        //when
        Reservation reservation = reservationService.getReservation(reservationId);

        //then
        assertEquals(expectedReservation, reservation);

    }

    @Test
    void 예약_조회_실패 () {
        //given
        Integer reservationId = 1;
        when(reservationRepository.findById(any())).thenReturn(null);
        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            reservationService.getReservation(reservationId);
        });

        //then
        assertEquals(CustomExceptionCode.RESERVATION_NOT_FOUND.getCode(), exception.getCustomExceptionCode().getCode());
        assertEquals(CustomExceptionCode.RESERVATION_NOT_FOUND.getMessage(), exception.getCustomExceptionCode().getMessage());
    }

    @Test
    void 예약_정보_저장_및_변경_성공() {
        //given
        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1,1 ,1 , Reservation.State.WAITING ,10000l, "A", LocalDateTime.now());
        when(reservationRepository.save(any())).thenReturn(expectedReservation);
        //when
        Reservation reservation = reservationService.changeReservationInfo(expectedReservation);
        //then
        assertEquals(expectedReservation, reservation);
    }

    @Test
    void 좌석예약_성공 () {
        //given
        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1,1 ,1 , Reservation.State.WAITING ,10000l, "A", LocalDateTime.now());

        when(reservationRepository.findReservedReservationBySeatId(any())).thenReturn(new ArrayList<>());
        when(reservationRepository.save(any())).thenReturn(expectedReservation);
        //when
        Reservation reservation = reservationService.reserveSeat(expectedReservation);

        //then
        assertEquals(expectedReservation, reservation);
    }

    @Test
    void 좌석예약_실패_예약자_존재 () {
        //given
        Reservation requestReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1,1 ,1 , Reservation.State.WAITING ,10000l, "A", LocalDateTime.now());

        Reservation prevReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1,1 ,1 , Reservation.State.WAITING ,10000l, "A", LocalDateTime.now());

        List<Reservation> reservationHistory = new ArrayList<>();
        reservationHistory.add(prevReservation);
        when(reservationRepository.findReservedReservationBySeatId(any())).thenReturn(reservationHistory);

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            reservationService.reserveSeat(requestReservation);
        });

        //then
        assertEquals(CustomExceptionCode.RESERVATION_EXIST.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.RESERVATION_EXIST.getMessage(), exception.getCustomExceptionCode().getMessage());

    }

}

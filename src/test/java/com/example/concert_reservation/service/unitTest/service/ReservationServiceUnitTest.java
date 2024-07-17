package com.example.concert_reservation.service.unitTest.service;


import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.fixture.ReservationFixture;
import com.example.concert_reservation.service.ReservationService;
import com.example.concert_reservation.service.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Reservation reservation = reservationService.getReservation(reservationId);

        //then
        assertEquals(null, reservation);
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
}

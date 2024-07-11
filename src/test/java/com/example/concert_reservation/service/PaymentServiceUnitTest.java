package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Payment;
import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceUnitTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PaymentService paymentService;

    @Test
    void 결제_성공 () {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        Reservation expectedReservation = new Reservation();
        expectedReservation.setState(Reservation.State.WAITING);
        expectedReservation.setSeatNo(1);
        expectedReservation.setId(1);
        expectedReservation.setPrice(10000l);
        expectedReservation.setConcertId(1);
        expectedReservation.setCreatedTime(LocalDateTime.now().minusMinutes(3));
        expectedReservation.setExpiredTime(LocalDateTime.now().plusMinutes(2));
        expectedReservation.setScheduleId(1);
        expectedReservation.setUserId(1);

        User expectedUser = new User();
        Point expectedPoint = new Point();
        expectedPoint.setAmount(20000l);
        expectedPoint.setId(1);
        expectedPoint.setUserId(1);
        expectedUser.setPoint(expectedPoint);
        expectedUser.setId(1);

        Payment expectedPayment = new Payment();
        expectedPayment.setCreatedTime(LocalDateTime.now());
        expectedPayment.setId(1);
        expectedPayment.setUserId(1);
        expectedPayment.setReservationId(1);

        when(reservationRepository.findById(any())).thenReturn(expectedReservation);
        when( userRepository.findById(any())).thenReturn(expectedUser);
        when( paymentRepository.save(any())).thenReturn(expectedPayment);

        //when
        Payment payment = paymentService.pay(requestPayment);

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

        when(reservationRepository.findById(any())).thenReturn(null);

        //when
        Throwable exception = assertThrows(NullPointerException.class, () -> {
            paymentService.pay(requestPayment);
        });
        //then
        assertEquals("예약 정보가 없습니다.", exception.getMessage().toString());

    }

    @Test
    void 결제_시간_만료() {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        Reservation expectedReservation = new Reservation();
        expectedReservation.setState(Reservation.State.EXPIRED);
        expectedReservation.setSeatNo(1);
        expectedReservation.setId(1);
        expectedReservation.setPrice(10000l);
        expectedReservation.setConcertId(1);
        expectedReservation.setCreatedTime(LocalDateTime.now().minusMinutes(8));
        expectedReservation.setExpiredTime(LocalDateTime.now().minusMinutes(3));
        expectedReservation.setScheduleId(1);
        expectedReservation.setUserId(1);
        when(reservationRepository.findById(any())).thenReturn(expectedReservation);

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(requestPayment);
        });
        //then
        assertEquals("결제 시간이 만료되었습니다.", exception.getMessage().toString());

    }


    @Test
    void 유저의_포인트정보_없음() {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        Reservation expectedReservation = new Reservation();
        expectedReservation.setState(Reservation.State.WAITING);
        expectedReservation.setSeatNo(1);
        expectedReservation.setId(1);
        expectedReservation.setPrice(10000l);
        expectedReservation.setConcertId(1);
        expectedReservation.setCreatedTime(LocalDateTime.now().minusMinutes(3));
        expectedReservation.setExpiredTime(LocalDateTime.now().plusMinutes(2));
        expectedReservation.setScheduleId(1);
        expectedReservation.setUserId(1);

        User expectUser = new User();

        when(reservationRepository.findById(any())).thenReturn(expectedReservation);
        when( userRepository.findById(any())).thenReturn(expectUser);


        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(requestPayment);
        });
        //then
        assertEquals("유저의 포인트 정보가 없습니다.", exception.getMessage().toString());

    }

    @Test
    void 유저의_포인트_부족() {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        Reservation expectedReservation = new Reservation();
        expectedReservation.setState(Reservation.State.WAITING);
        expectedReservation.setSeatNo(1);
        expectedReservation.setId(1);
        expectedReservation.setPrice(10000l);
        expectedReservation.setConcertId(1);
        expectedReservation.setCreatedTime(LocalDateTime.now().minusMinutes(3));
        expectedReservation.setExpiredTime(LocalDateTime.now().plusMinutes(2));
        expectedReservation.setScheduleId(1);
        expectedReservation.setUserId(1);

        User expectUser = new User();
        expectUser.setId(1);
        Point expectPoint = new Point();
        expectPoint.setUserId(1);
        expectPoint.setId(1);
        expectPoint.setAmount(1000l);
        expectUser.setPoint(expectPoint);

        when(reservationRepository.findById(any())).thenReturn(expectedReservation);
        when( userRepository.findById(any())).thenReturn(expectUser);


        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(requestPayment);
        });
        //then
        assertEquals("유저의 포인트가 결제금액보다 적습니다.", exception.getMessage().toString());

    }

}

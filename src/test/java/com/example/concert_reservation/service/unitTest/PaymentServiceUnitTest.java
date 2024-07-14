package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.entity.*;
import com.example.concert_reservation.fixture.PaymentFixture;
import com.example.concert_reservation.fixture.ReservationFixture;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.service.PaymentService;
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

    @Mock
    PointRepository pointRepository;

    @Mock
    TokenRepository tokenRepository;

    @InjectMocks
    PaymentService paymentService;

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

        when(reservationRepository.findById(any())).thenReturn(expectedReservation);
        when( userRepository.findById(any())).thenReturn(expectedUser);
        when(pointRepository.save(any())).thenReturn(expectedUser.getPoint());
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

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1, 1, 1, Reservation.State.EXPIRED, 10000l, "A", LocalDateTime.now().minusMinutes(8));

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

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1, 1, 1, Reservation.State.WAITING, 10000l, "A", LocalDateTime.now().minusMinutes(3));


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

        Reservation expectedReservation =
                ReservationFixture.creasteReservation(1, 1, 1, 1, 1, 1, Reservation.State.WAITING, 10000l, "A", LocalDateTime.now().minusMinutes(3));


        User expectUser = UserFixture.createUser(1, "user1", 1, 1000l);

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

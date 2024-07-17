package com.example.concert_reservation.service.IntegrationTest;


import com.example.concert_reservation.domain.entity.Payment;
import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.fixture.PaymentFixture;
import com.example.concert_reservation.fixture.ReservationFixture;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.domain.service.PaymentService;
import com.example.concert_reservation.domain.service.repository.PointRepository;
import com.example.concert_reservation.domain.service.repository.ReservationRepository;
import com.example.concert_reservation.domain.service.repository.SeatRepository;
import com.example.concert_reservation.domain.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class PaymentServiceIntegrationTest {


    @Autowired
    PaymentService paymentService;

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
    void 결제_성공() {
        //given
        Integer userId = 1;

        Reservation reservation =
                ReservationFixture.creasteReservation(null, userId, 1, 1, 1, 1, Reservation.State.WAITING, 8000l, "A", LocalDateTime.now());
        reservation = reservationRepository.save(reservation);

        Payment payment = PaymentFixture.createPayment(null, userId, reservation.getId(), LocalDateTime.now());

        //when
        payment = paymentService.pay(payment);

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
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(payment);
        });

        //then
        User user = userRepository.findById(userId);
        assertEquals("예약 정보가 없습니다.", exception.getMessage().toString());
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
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(payment);
        });

        //then
        User user = userRepository.findById(userId);
        assertEquals("결제 시간이 만료되었습니다.", exception.getMessage().toString());
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
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(payment);
        });

        //then
        User user = userRepository.findById(userId);
        reservation = reservationRepository.findById(reservation.getId());
        assertEquals("유저의 포인트가 결제금액보다 적습니다.", exception.getMessage().toString());
        assertEquals(10000, user.getPoint().getAmount());
        assertEquals(Reservation.State.WAITING, reservation.getState());
    }




}

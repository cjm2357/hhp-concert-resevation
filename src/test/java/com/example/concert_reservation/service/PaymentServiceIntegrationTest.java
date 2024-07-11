package com.example.concert_reservation.service;


import com.example.concert_reservation.entity.Payment;
import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.repository.PointRepository;
import com.example.concert_reservation.service.repository.ReservationRepository;
import com.example.concert_reservation.service.repository.SeatRepository;
import com.example.concert_reservation.service.repository.UserRepository;
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
        User user = new User();
        user.setId(1);
        user.setName("유저1");
        Point point = new Point();
        point.setAmount(10000l);
        point.setUserId(1);
        point = pointRepository.save(point);
        user.setPoint(point);
        userRepository.save(user);
    }

    @Test
    void 결제_성공() {
        //given
        Integer userId = 1;

        Reservation reservation = new Reservation();
        reservation.setConcertId(1);
        reservation.setScheduleId(1);
        reservation.setSeatId(1);
        reservation.setSeatNo(1);
        reservation.setState(Reservation.State.WAITING);
        reservation.setPrice(8000l);
        reservation.setCreatedTime(LocalDateTime.now());
        reservation.setExpiredTime(LocalDateTime.now().plusMinutes(Reservation.EXPIRE_TIME_FIVE_MIN));
        reservation.setSeatGrade("A");
        reservation.setUserId(userId);
        reservation = reservationRepository.save(reservation);

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setReservationId(reservation.getId());

        //when
        payment = paymentService.pay(payment);

        //then
        User user = new User();
        user.setId(userId);
        user = userRepository.findById(user);
        reservation = reservationRepository.findById(reservation.getId());
        assertNotNull(payment.getId());
        assertEquals(10000-8000, user.getPoint().getAmount());
        assertEquals(Reservation.State.COMPLETED, reservation.getState());
    }

    @Test
    void 예약정보없음() {
        //given
        Integer userId = 1;


        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setReservationId(1);

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(payment);
        });

        //then
        User user = new User();
        user.setId(userId);
        user = userRepository.findById(user);
        assertEquals("예약 정보가 없습니다.", exception.getMessage().toString());
        assertEquals(10000, user.getPoint().getAmount());

    }


    @Test
    void 결제시간만료() {
        //given
        Integer userId = 1;

        Reservation reservation = new Reservation();
        reservation.setConcertId(1);
        reservation.setScheduleId(1);
        reservation.setSeatId(1);
        reservation.setSeatNo(1);
        reservation.setState(Reservation.State.EXPIRED);
        reservation.setPrice(8000l);
        reservation.setCreatedTime(LocalDateTime.now().minusMinutes(10));
        reservation.setExpiredTime(LocalDateTime.now().minusMinutes(5));
        reservation.setSeatGrade("A");
        reservation.setUserId(userId);
        reservation = reservationRepository.save(reservation);

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setReservationId(reservation.getId());

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(payment);
        });

        //then
        User user = new User();
        user.setId(userId);
        user = userRepository.findById(user);
        assertEquals("결제 시간이 만료되었습니다.", exception.getMessage().toString());
        assertEquals(10000, user.getPoint().getAmount());

    }


    @Test
    void 잔액부족() {
        //given
        Integer userId = 1;

        Reservation reservation = new Reservation();
        reservation.setConcertId(1);
        reservation.setScheduleId(1);
        reservation.setSeatId(1);
        reservation.setSeatNo(1);
        reservation.setState(Reservation.State.WAITING);
        reservation.setPrice(12000l);
        reservation.setCreatedTime(LocalDateTime.now());
        reservation.setExpiredTime(LocalDateTime.now().plusMinutes(Reservation.EXPIRE_TIME_FIVE_MIN));
        reservation.setSeatGrade("A");
        reservation.setUserId(userId);
        reservation = reservationRepository.save(reservation);

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setReservationId(reservation.getId());

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            paymentService.pay(payment);
        });

        //then
        User user = new User();
        user.setId(userId);
        user = userRepository.findById(user);
        reservation = reservationRepository.findById(reservation.getId());
        assertEquals("유저의 포인트가 결제금액보다 적습니다.", exception.getMessage().toString());
        assertEquals(10000, user.getPoint().getAmount());
        assertEquals(Reservation.State.WAITING, reservation.getState());
    }




}

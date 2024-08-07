package com.example.concert_reservation.application.concert;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.*;
import com.example.concert_reservation.domain.service.concert.ConcertService;
import com.example.concert_reservation.domain.service.payment.PaymentService;
import com.example.concert_reservation.domain.service.point.PointService;
import com.example.concert_reservation.domain.service.reservation.ReservationService;
import com.example.concert_reservation.domain.service.schedule.ScheduleService;
import com.example.concert_reservation.domain.service.seat.SeatService;
import com.example.concert_reservation.domain.service.token.TokenService;
import com.example.concert_reservation.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConcertFacade {

    private final UserService userService;
    private final PointService pointService;
    private final ConcertService concertService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final TokenService tokenService;


    public List<Concert> getConcertList() {
        return concertService.getConcertList();
    }

    public List<Schedule> getAvailableScheduleList(Integer concertId) {
        List<Schedule> schedules = scheduleService.getScheduleListByConcertId(concertId);
        List<Seat> seats = seatService.getAvailableSeatListByConcertId(concertId);
        schedules = schedules.stream()
                .filter(schedule ->
                        seats.stream().filter(seat-> seat.getScheduleId() == schedule.getId()
                        ).findAny().isPresent()
                ).toList();
        return schedules;
    }

    public List<Seat> getAvailableSeatList(Integer scheduleId) {
        return seatService.getAvailableSeatList(scheduleId);
    }

    public Reservation reserveSeat(Integer seatId, Integer userId) {
        //user 존재여부 확인
        User user = userService.getUser(userId);
        Seat seat = null;

        try {
            seat = seatService.updateSeatState(seatId, Seat.State.RESERVED);
        } catch (Exception e) {
            log.warn("user {} fail reservation {} seat", userId, seatId);
            throw new CustomException(CustomExceptionCode.RESERVATION_FAILED);
        }
        Reservation reservation = new Reservation();
        reservation.enrollSeatInfoForReservation(userId, seat);
        reservation = reservationService.reserveSeat(reservation);
        log.info("{} user success to reserve {} seat", userId, seatId);
        return reservation;
    }

    public Payment pay(Payment payment, UUID tokenKey) {
        Reservation reservation = reservationService.getReservation(payment.getReservationId());
        reservation.isNotExpired();

        //예약한 유저인지 확인
        User user = userService.getUser(payment.getUserId());
        user.isReservationUser(reservation.getUserId());
        //지불 가능한지 확인
        user.isPayable(reservation.getPrice());
        payment = paymentService.pay(payment);

        //포인트 차감
        pointService.payPoint(user, reservation.getPrice());

        reservation.setState(Reservation.State.COMPLETED);
        seatService.updateSeatState(reservation.getSeatId(), Seat.State.RESERVED);
        reservationService.changeReservationInfo(reservation);
        tokenService.expireToken(tokenKey);

        log.info("{} user success to pay {} reservation", user.getId(), reservation.getId());
        return payment;
    }

    @Transactional
    public void expireReservation() {
        //결제시간이 만료된 예약을 취소시킨다
        List<Reservation> reservations = reservationService.readExpiredReservation();
        List<Integer> seatIdList = new ArrayList<>();
        reservations.stream().forEach(reservation -> {
            seatIdList.add(reservation.getSeatId());
            reservation.setState(Reservation.State.EXPIRED);
        });

        seatService.saveAllSeatState(seatIdList, Seat.State.EMPTY);
    }


}
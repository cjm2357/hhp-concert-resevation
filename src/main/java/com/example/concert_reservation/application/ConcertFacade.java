package com.example.concert_reservation.application;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.*;
import com.example.concert_reservation.domain.service.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ConcertFacade {

    private final UserService userService;
    private final PointService pointService;
    private final ConcertService concertService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final TokenService tokenService;

    public ConcertFacade(
            UserService userService,
            PointService pointService,
            ConcertService concertService,
            ScheduleService scheduleService,
            SeatService seatService,
            ReservationService reservationService,
            PaymentService paymentService,
            TokenService tokenService
    ) {
        this.userService = userService;
        this.pointService = pointService;
        this.concertService = concertService;
        this.scheduleService = scheduleService;
        this.seatService = seatService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.tokenService = tokenService;
    }

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

    @Transactional
    public Reservation reserveSeat(Integer seatId, Integer userId) {
        User user = userService.getUser(userId);
        Reservation reservation = enrollSeatAndState(seatId, userId);
        log.info("{} user success to reserve {} seat", userId, seatId);
        return reservation;
    }

    @Transactional
    public Reservation enrollSeatAndState(Integer seatId, Integer userId) {
        Seat seatInfo = seatService.getSeatById(seatId);
        Reservation reservation = new Reservation();
        reservation.enrollSeatInfoForReservation(userId, seatInfo);
        reservation = reservationService.reserveSeat(reservation);
        if (reservation == null || reservation.getSeatId() == null) {
            log.warn("failed reservation");
            throw new CustomException(CustomExceptionCode.RESERVATION_FAILED);
        }
        seatInfo.setState(Seat.State.RESERVED);
        seatService.updateSeat(seatInfo);
        return reservation;
    }
    //        seatService.saveSeatState(reservation.getSeatId(), Seat.State.RESERVED);

    @Transactional
    public Payment pay(Payment payment) {
        Reservation reservation = reservationService.getReservation(payment.getReservationId());
        reservation.isNotExpired();

        User user = userService.getUser(payment.getUserId());
        user.isReservationUser(reservation.getUserId());
        user.isPayable(reservation.getPrice());
        
        payment.setCreatedTime(LocalDateTime.now());
        payment = paymentService.pay(payment);

        
        //lock적용범위
        Point userPoint = user.getPoint();
        userPoint.setAmount(userPoint.getAmount() - reservation.getPrice());
        pointService.chargePoint(userPoint);
        //lock적용범위

        reservation.setState(Reservation.State.COMPLETED);
        reservationService.changeReservationInfo(reservation);
//        seatService.saveSeatState(reservation.getSeatId(), Seat.State.RESERVED);

        tokenService.updateStateToExpiredByUserId(user.getId());
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

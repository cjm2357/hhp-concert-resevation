package com.example.concert_reservation.application;

import com.example.concert_reservation.domain.entity.*;
import com.example.concert_reservation.domain.service.*;
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
        Seat seatInfo = seatService.getSeatById(seatId);
        Reservation reservation = new Reservation();
        reservation.enrollSeatInfoForReservation(userId, seatInfo);
        return reservationService.reserveSeat(reservation);
    }

    @Transactional
    public Payment pay(Payment payment) {
        Reservation reservation = reservationService.getReservation(payment.getReservationId());
        if (reservation == null) {
            log.warn("no reservation information");
            throw new NullPointerException("예약 정보가 없습니다.");
        }
        if (reservation.getState() == Reservation.State.EXPIRED || reservation.getExpiredTime().isBefore(LocalDateTime.now())) {
            log.warn("{} user, payment time expired", payment.getUserId());
            throw new RuntimeException("결제 시간이 만료되었습니다.");
        }

        User user = userService.getUser(payment.getUserId());

        if (user.getPoint() == null) {
            log.warn("{} user, no point information", user.getId());
            throw new RuntimeException("유저의 포인트 정보가 없습니다.");
        }
        if (user.getPoint().getAmount() < reservation.getPrice()) {
            log.warn("{} user, points are less than the payment amount");
            throw new RuntimeException("유저의 포인트가 결제금액보다 적습니다.");
        }

        payment.setCreatedTime(LocalDateTime.now());
        payment = paymentService.pay(payment);

        Point userPoint = user.getPoint();
        userPoint.setAmount(userPoint.getAmount() - reservation.getPrice());
        pointService.chargePoint(userPoint);

        reservation.setState(Reservation.State.COMPLETED);
        reservationService.changeReservationInfo(reservation);
        seatService.saveSeatState(reservation.getSeatId(), Seat.State.RESERVED);

        tokenService.updateStateToExpiredByUserId(user.getId());

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

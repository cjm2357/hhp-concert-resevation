package com.example.concert_reservation.presentation.controller.concert;


import com.example.concert_reservation.application.concert.ConcertFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.*;
import com.example.concert_reservation.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
public class ConcertController {


   private final ConcertFacade concertFacade;


   public ConcertController(ConcertFacade concertFacade) {
       this.concertFacade = concertFacade;
   }
    @GetMapping("/concerts")
    public ResponseEntity<?> getConcertList() {
        List<Concert> concertList = concertFacade.getConcertList();
        return ResponseEntity.ok(new ConcertResponseDto(concertList));
    }

    @GetMapping("/concerts/{concertId}/schedules")
    public ResponseEntity<?> readSchedule(
            @PathVariable("concertId") Integer concertId) {

        if (concertId == null || concertId < 0) {
            log.warn("read schedule bad request");
            throw new CustomException(CustomExceptionCode.INVALID_CONCERT_ID);
        }
        List<Schedule> schedules = concertFacade.getAvailableScheduleList(concertId);
        return ResponseEntity.ok(new ScheduleResponseDto(schedules));

    }

    // 예약가능 Seat 조회 API
    @GetMapping("schedules/{scheduleId}/seats")
    public ResponseEntity<?> readSeat(
            @PathVariable("scheduleId") Integer scheduleId
    ) {
       if (scheduleId == null || scheduleId < 0) {
           log.warn("read seat bad request");
           throw new CustomException(CustomExceptionCode.INVALID_SCHEDULE_ID);

       }
        List<Seat> seats = concertFacade.getAvailableSeatList(scheduleId);
        return ResponseEntity.ok(new SeatResponseDto(seats));

    }

    @PostMapping("/seats/{seatId}/reservation")
    public ResponseEntity<?> reserveSeat(
            @PathVariable("seatId") Integer seatId,
            @RequestBody SeatReservationRequestDto requestDto
    ) {
       if (requestDto.getUserId() == null || requestDto.getUserId() < 0) {
           log.warn("no user id");
           throw new CustomException(CustomExceptionCode.USER_CAN_NOT_BE_NULL);
       }

        if (seatId == null || seatId < 0) {
            log.warn("{} user, reserve seat bad request", requestDto.getUserId());
            throw new CustomException(CustomExceptionCode.INVALID_SEAT_ID);
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(requestDto.getUserId());
        reservation.setSeatId(seatId);
        reservation = concertFacade.reserveSeat(seatId, requestDto.getUserId());
        SeatReservationResponseDto responseDto = new SeatReservationResponseDto(reservation);
        return ResponseEntity.ok(responseDto);

    }

        // 결제 API
    @PostMapping("/payment")
    public ResponseEntity<?> pay(
            @RequestBody PaymentRequestDto dto,
            @RequestHeader(value = "Authorization", required = true) UUID tokenKey
    ) {
        if (dto.getUserId() == null || dto.getUserId() < 0) {
            log.warn("no user id");
            throw new CustomException(CustomExceptionCode.USER_CAN_NOT_BE_NULL);
        }
        if (dto.getReservationId() == null || dto.getReservationId() < 0) {
            log.warn("{} user is failed to pay, no reservation id", dto.getUserId());
            throw new CustomException(CustomExceptionCode.INVALID_RESERVATION_ID);
        }

        Payment payment = concertFacade.pay(dto.toEntity(), tokenKey);
        PaymentResponseDto responseDto = new PaymentResponseDto(payment);
        return ResponseEntity.ok(responseDto);
    }
}

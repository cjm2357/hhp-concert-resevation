package com.example.concert_reservation.presentation.controller;


import com.example.concert_reservation.application.ConcertFacade;
import com.example.concert_reservation.domain.entity.*;
import com.example.concert_reservation.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        if (concertId != null && concertId >= 0) {
            List<Schedule> schedules = concertFacade.getAvailableScheduleList(concertId);
            return ResponseEntity.ok(new ScheduleResponseDto(schedules));
        } else {
            log.warn("read schedule bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no concert ID");
        }

    }

    // 예약가능 Seat 조회 API
    @GetMapping("schedules/{scheduleId}/seats")
    public ResponseEntity<?> readSeat(
            @PathVariable("scheduleId") Integer scheduleId
    ) {
        if (scheduleId != null && scheduleId > 0) {
            List<Seat> seats = concertFacade.getAvailableSeatList(scheduleId);
            return ResponseEntity.ok(new SeatResponseDto(seats));
        }
        log.warn("read seat bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("no schedule ID");

    }

    @PostMapping("/seats/{seatId}/reservation")
    public ResponseEntity<?> reserveSeat(
            @PathVariable("seatId") Integer seatId,
            @RequestBody SeatReservationRequestDto requestDto
    ) {
        if (seatId != null && seatId > 0) {
            Reservation reservation = new Reservation();
            reservation.setUserId(requestDto.getUserId());
            reservation.setSeatId(seatId);
            reservation = concertFacade.reserveSeat(seatId, requestDto.getUserId());
            SeatReservationResponseDto responseDto = new SeatReservationResponseDto(reservation);
            return ResponseEntity.ok(responseDto);
        }
        log.warn("{} user, reserve seat bad request", requestDto.getUserId());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("no seat ID");

    }

        // 결제 API
    @PostMapping("/payment")
    public ResponseEntity<?> pay(
            @RequestBody PaymentRequestDto dto
    ) {
        if (dto.getReservationId() != null && dto.getReservationId() > 0) {
            Payment payment = concertFacade.pay(dto.toEntity());
            PaymentResponseDto responseDto = new PaymentResponseDto(payment);
            return ResponseEntity.ok(responseDto);
        }
        log.warn("{} user is failed to pay, no reservation id", dto.getUserId());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no reservation ID");
    }
}

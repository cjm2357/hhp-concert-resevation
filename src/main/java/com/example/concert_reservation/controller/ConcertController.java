package com.example.concert_reservation.controller;


import com.example.concert_reservation.application.ConcertFacade;
import com.example.concert_reservation.dto.*;
import com.example.concert_reservation.entity.Concert;
import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.Schedule;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.manager.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ConcertController {


   private final ConcertFacade concertFacade;

   private final TokenManager tokenManager;

   public ConcertController(ConcertFacade concertFacade, TokenManager tokenManager) {
       this.concertFacade = concertFacade;
       this.tokenManager = tokenManager;
   }
    @GetMapping("/concerts")
    public ResponseEntity<?> getConcertList() {
        List<Concert> concertList = concertFacade.getConcertList();
        return ResponseEntity.ok(new ConcertResponseDto(concertList));
    }

    @GetMapping("/concerts/{concertId}/schedules")
    public ResponseEntity<?> readSchedule(
            @RequestHeader(value = "Authorization", required = false) UUID key,
            @PathVariable("concertId") Integer concertId) {
        try {
            tokenManager.validateToken(key);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
        }

        if (concertId != null && concertId >= 0) {
            List<Schedule> schedules = concertFacade.getAvailableScheduleList(concertId);
            return ResponseEntity.ok(new ScheduleResponseDto(schedules));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no concert ID");
        }

    }

    // 예약가능 Seat 조회 API
    @GetMapping("schedules/{scheduleId}/seats")
    public ResponseEntity<?> readSeat(
            @RequestHeader(value = "Authorization", required = false) UUID key,
            @PathVariable("scheduleId") Integer scheduleId
    ) {
        try {
            tokenManager.validateToken(key);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
        }
        if (scheduleId != null && scheduleId > 0) {
            List<Seat> seats = concertFacade.getAvailableSeatList(scheduleId);
            return ResponseEntity.ok(new SeatResponseDto(seats));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("no schedule ID");

    }

    @PostMapping("/seats/{seatId}/reservation")
    public ResponseEntity<?> reserveSeat(
            @RequestHeader(value = "Authorization", required = false) UUID key,
            @PathVariable("seatId") Integer seatId,
            @RequestBody SeatReservationRequestDto requestDto
    ) {
        try {
            tokenManager.validateToken(key);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
        }
        if (seatId != null && seatId > 0) {
            Reservation reservation = new Reservation();
            reservation.setUserId(requestDto.getUserId());
            reservation.setSeatId(seatId);
            reservation = concertFacade.reserveSeat(seatId, requestDto.getUserId());
            SeatReservationResponseDto responseDto = new SeatReservationResponseDto(reservation);
            return ResponseEntity.ok(responseDto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("no seat ID");


    }
}

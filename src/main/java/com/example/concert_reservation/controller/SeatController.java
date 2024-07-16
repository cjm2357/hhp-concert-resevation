//package com.example.concert_reservation.controller;
//
//import com.example.concert_reservation.dto.SeatReservationRequestDto;
//import com.example.concert_reservation.dto.SeatReservationResponseDto;
//import com.example.concert_reservation.dto.SeatResponseDto;
//import com.example.concert_reservation.entity.Reservation;
//import com.example.concert_reservation.entity.Seat;
//import com.example.concert_reservation.manager.TokenManager;
//import com.example.concert_reservation.service.SeatService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.security.sasl.AuthenticationException;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api")
//public class SeatController {
//
//
//    private final SeatService seatService;
//    private final TokenManager tokenManager;
//
//    public SeatController(SeatService seatService, TokenManager tokenManager) {
//        this.seatService = seatService;
//        this.tokenManager = tokenManager;
//    }
//
//    // 예약가능 Seat 조회 API
//    @GetMapping("schedules/{scheduleId}/seats")
//    public ResponseEntity<?> readSeat(
//            @RequestHeader(value = "Authorization", required = false) UUID key,
//            @PathVariable("scheduleId") Integer scheduleId
//    ) {
//        try {
//            tokenManager.validateToken(key);
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
//        }
//        if (scheduleId != null && scheduleId > 0) {
//            List<Seat> seats = seatService.getAvailableSeatList(scheduleId);
//            return ResponseEntity.ok(new SeatResponseDto(seats));
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body("no schedule ID");
//
//    }
//
//    //좌석 예약 API
//    @PostMapping("/seats/{seatId}/reservation")
//    public ResponseEntity<?> reserveSeat(
//            @RequestHeader(value = "Authorization", required = false) UUID key,
//            @PathVariable("seatId") Integer seatId,
//            @RequestBody SeatReservationRequestDto requestDto
//    ) {
//        try {
//            tokenManager.validateToken(key);
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
//        }
//        if (seatId != null && seatId > 0) {
//            Reservation reservation = new Reservation();
//            reservation.setUserId(requestDto.getUserId());
//            reservation.setSeatId(seatId);
//            reservation = seatService.reserveSeat(reservation);
//            SeatReservationResponseDto responseDto = new SeatReservationResponseDto(reservation);
//            return ResponseEntity.ok(responseDto);
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body("no seat ID");
//
//
//    }
//
//}

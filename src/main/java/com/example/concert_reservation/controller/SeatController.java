package com.example.concert_reservation.controller;

import com.example.concert_reservation.dto.SeatRequestDto;
import com.example.concert_reservation.dto.SeatReservationRequestDto;
import com.example.concert_reservation.dto.SeatReservationResponseDto;
import com.example.concert_reservation.dto.SeatResponseDto;
import com.example.concert_reservation.entity.Seat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SeatController {

    // 예약가능 Seat 조회 API
    @PostMapping("/seat")
    public ResponseEntity<?> readSeat(
            @RequestHeader(value = "Authorization", required = false) UUID key,
            @RequestBody SeatRequestDto dto
    ) {
        if (key != null) {
            if (dto.getScheduleId() != null) {
                SeatResponseDto responseDto = new SeatResponseDto();
                List<Seat> seats = new ArrayList<>();

                Seat seat1 = new Seat();
                seat1.setId(1);
                seat1.setConcertId(1);
                seat1.setScheduleId(dto.getScheduleId());
                seat1.setSeatNo(1);
                seat1.setState("RESERVED");
                seat1.setGrade("S");
                seat1.setPrice(50000l);
                seats.add(seat1);

                Seat seat2 = new Seat();
                seat2.setId(2);
                seat2.setConcertId(1);
                seat2.setScheduleId(dto.getScheduleId());
                seat2.setSeatNo(2);
                seat2.setState("EMPTY");
                seat2.setGrade("A");
                seat2.setPrice(30000l);
                seats.add(seat2);

                responseDto.setSeats(seats);
                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no schedule ID");

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("invalid token");
        }
    }

    //좌석 예약 API
    @PostMapping("/seat/reservation")
    public ResponseEntity<?> reserveSeat(
            @RequestHeader(value = "Authorization", required = false) UUID key,
            @RequestBody SeatReservationRequestDto dto
    ) {
        if (key != null) {
            if (dto.getSeatId() != null) {
                SeatReservationResponseDto responseDto = new SeatReservationResponseDto();
                responseDto.setSuccess(true);

                Seat seat = new Seat();
                seat.setId(1);
                seat.setConcertId(1);
                seat.setScheduleId(1);
                seat.setSeatNo(1);
                seat.setState("RESERVED");
                seat.setGrade("S");
                seat.setPrice(50000l);

                responseDto.setSeat(seat);


                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no seat ID");

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("invalid token");
        }
    }

}

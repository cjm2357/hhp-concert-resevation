package com.example.concert_reservation.controller;

import com.example.concert_reservation.dto.PaymentRequestDto;
import com.example.concert_reservation.dto.PaymentResponseDto;
import com.example.concert_reservation.dto.SeatReservationResponseDto;
import com.example.concert_reservation.entity.Seat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PaymentController {

    // 결제 API
    @PostMapping("/payment")
    public ResponseEntity<?> pay(
            @RequestHeader(value = "Authorization", required = false) UUID key,
            @RequestBody PaymentRequestDto dto
    ) {
        if (key != null) {
            if (dto.getReservationId() != null) {
                PaymentResponseDto responseDto = new PaymentResponseDto();
                responseDto.setSuccess(true);
                responseDto.setReservationId(dto.getReservationId());
                responseDto.setConcertName("아이유 콘서트");
                responseDto.setSeatPrice(100000l);
                responseDto.setSeatNo(1);

                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no reservation ID");

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("invalid token");
        }
    }
}

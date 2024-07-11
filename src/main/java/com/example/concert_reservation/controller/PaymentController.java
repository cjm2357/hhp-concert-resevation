package com.example.concert_reservation.controller;

import com.example.concert_reservation.dto.PaymentRequestDto;
import com.example.concert_reservation.dto.PaymentResponseDto;
import com.example.concert_reservation.dto.SeatReservationResponseDto;
import com.example.concert_reservation.entity.Payment;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.manager.TokenManager;
import com.example.concert_reservation.service.PaymentService;
import com.example.concert_reservation.service.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;
    private final TokenManager tokenManager;

    public PaymentController (PaymentService paymentService, TokenManager tokenManager) {
        this.paymentService = paymentService;
        this.tokenManager = tokenManager;
    }

    // 결제 API
    @PostMapping("/payment")
    public ResponseEntity<?> pay(
            @RequestHeader(value = "Authorization", required = false) UUID key,
            @RequestBody PaymentRequestDto dto
    ) {
        try {
            tokenManager.validateToken(key);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
        }

        if (dto.getReservationId() != null && dto.getReservationId() > 0) {
                Payment payment = paymentService.pay(dto.toEntity());
                PaymentResponseDto responseDto = new PaymentResponseDto(payment);

                return ResponseEntity.ok(responseDto);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no reservation ID");


    }
}

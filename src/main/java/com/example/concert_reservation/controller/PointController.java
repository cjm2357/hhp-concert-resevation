package com.example.concert_reservation.controller;

import com.example.concert_reservation.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PointController {

    // 포인트 조회 API
    @PostMapping("/point")
    public ResponseEntity<?> readPoint(@RequestBody PointRequestDto dto) {
        if (dto.getUserId() != null) {
            PointResponseDto responseDto = new PointResponseDto();
            responseDto.setUserId(dto.getUserId());
            responseDto.setAmount(100000l);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no user ID");
        }
    }

    // 포인트 충전 API
    @PostMapping("/point/charge")
    public ResponseEntity<?> chargePoint(@RequestBody PointChargeRequestDto dto) {
        if (dto.getUserId() != null && dto.getAmount() !=null || dto.getAmount() <= 0) {
            PointChargeResponseDto responseDto = new PointChargeResponseDto();
            responseDto.setUserId(dto.getUserId());
            responseDto.setAmount(10000 + dto.getAmount());
            return ResponseEntity.ok(responseDto);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("bad request");
        }

    }
}

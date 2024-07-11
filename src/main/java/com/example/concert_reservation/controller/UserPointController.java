package com.example.concert_reservation.controller;

import com.example.concert_reservation.dto.UserPointRequestDto;
import com.example.concert_reservation.dto.UserPointResponseDto;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.UserPointService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserPointController {

    private final UserPointService userPointService;

    public UserPointController(UserPointService userPointService) {
        this.userPointService = userPointService;
    }

    // 포인트 조회 API
    @PostMapping("/points")
    public ResponseEntity<?> readPoint(@RequestBody UserPointRequestDto dto) {
        if (dto.getUserId() != null) {
                User user = userPointService.getPoint(dto.toEntity());
            return ResponseEntity.ok(new UserPointResponseDto(user));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no user ID");
        }
    }

    // 포인트 충전 API
    @PostMapping("/points/charge")
    public ResponseEntity<?> chargePoint(@RequestBody UserPointRequestDto dto) {
        if (dto.getUserId() != null && dto.getAmount() !=null && dto.getAmount() >= 0) {
            User user = userPointService.chargePoint(dto.toEntity(), dto.getAmount());
            UserPointResponseDto responseDto = new UserPointResponseDto(user);
            return ResponseEntity.ok(responseDto);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("bad request");
        }

    }
}

package com.example.concert_reservation.presentation.controller;

import com.example.concert_reservation.application.UserPointFacade;
import com.example.concert_reservation.dto.UserPointRequestDto;
import com.example.concert_reservation.dto.UserPointResponseDto;
import com.example.concert_reservation.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserPointController {

    private final UserPointFacade userPointFacade;

    public UserPointController(UserPointFacade userPointFacade) {
        this.userPointFacade = userPointFacade;
    }

    // 포인트 조회 API
    @PostMapping("/points")
    public ResponseEntity<?> readPoint(@RequestBody UserPointRequestDto dto) {
        if (dto.getUserId() != null) {
                User user = userPointFacade.getUserWithPoint(dto.getUserId());
            return ResponseEntity.ok(new UserPointResponseDto(user));
        } else {
            log.warn("no user id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no user ID");
        }
    }

    // 포인트 충전 API
    @PostMapping("/points/charge")
    public ResponseEntity<?> chargePoint(@RequestBody UserPointRequestDto dto) {
        if (dto.getUserId() == null) {
            log.warn("no user id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("bad request no user id");
        }

        if (dto.getAmount() ==null || dto.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("bad request invalid charge amount");
        }

        User user = userPointFacade.chargePoint(dto.toEntity(), dto.getAmount());
        UserPointResponseDto responseDto = new UserPointResponseDto(user);
        return ResponseEntity.ok(responseDto);



    }
}

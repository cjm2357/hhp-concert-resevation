package com.example.concert_reservation.controller;

import com.example.concert_reservation.dto.ScheduleResponseDto;
import com.example.concert_reservation.entity.Schedule;
import com.example.concert_reservation.manager.TokenManager;
import com.example.concert_reservation.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final TokenManager tokenManager;

    public ScheduleController(ScheduleService scheduleService, TokenManager tokenManager) {
        this.scheduleService = scheduleService;
        this.tokenManager = tokenManager;
    }
    // 날짜 조회 API
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
                List<Schedule> schedules = scheduleService.getAvailableScheduleList(concertId);
                return ResponseEntity.ok(new ScheduleResponseDto(schedules));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("no concert ID");
            }

    }

}

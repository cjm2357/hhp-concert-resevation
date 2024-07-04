package com.example.concert_reservation.controller;

import com.example.concert_reservation.dto.ScheduleRequestDto;
import com.example.concert_reservation.dto.ScheduleResponseDto;
import com.example.concert_reservation.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    // 날짜 조회 API
    @PostMapping("/schedule")
    public ResponseEntity<?> readSchedule(@RequestBody ScheduleRequestDto dto) {
        if (dto.getConcertId() != null) {
            ScheduleResponseDto responseDto = new ScheduleResponseDto();
            List<Schedule> schedules = new ArrayList<>();
            Schedule schedule1 = new Schedule();
            schedule1.setId(1);
            schedule1.setConcertId(dto.getConcertId());
            schedule1.setDate(LocalDateTime.of(2024, 3,3,12,0,0));
            schedules.add(schedule1);

            Schedule schedule2 = new Schedule();
            schedule2.setId(2);
            schedule2.setConcertId(dto.getConcertId());
            schedule2.setDate(LocalDateTime.of(2024, 3,4,12,0,0));
            schedules.add(schedule2);

            responseDto.setSchedulesList(schedules);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("no concert ID");
        }
    }

}

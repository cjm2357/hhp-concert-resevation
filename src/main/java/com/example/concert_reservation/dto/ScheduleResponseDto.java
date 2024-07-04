package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleResponseDto {
    private List<Schedule> schedulesList;
}

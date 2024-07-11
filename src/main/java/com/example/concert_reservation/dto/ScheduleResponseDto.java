package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScheduleResponseDto {
    private List<ScheduleInfo> schedulesList = new ArrayList<>();

    @Getter
    @Setter
    public static class ScheduleInfo {
        private Integer scheduleId;
        private LocalDateTime date;

    }

    public ScheduleResponseDto(List<Schedule> schedulesList) {
        schedulesList.stream().forEach(schedule -> {
            ScheduleInfo scheduleInfo = new ScheduleInfo();
            scheduleInfo.setScheduleId(schedule.getId());
            scheduleInfo.setDate(schedule.getDate());
            this.schedulesList.add(scheduleInfo);
        });
    }
}

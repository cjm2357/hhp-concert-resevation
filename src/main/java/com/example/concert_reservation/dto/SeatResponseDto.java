package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.Seat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SeatResponseDto {

    private List<SeatInfo> seats = new ArrayList<>();

    @Getter
    @Setter
    public static class SeatInfo {
        private Integer seatId;
        private Integer concertId;
        private Integer scheduleId;
        private Integer seatNo;
        private Seat.State state;
        private String grade;
        private Long price;

    }

    public SeatResponseDto(List<Seat> seats) {
        seats.stream().forEach(seat -> {
            SeatInfo seatInfo = new SeatInfo();
            seatInfo.setSeatId(seat.getId());
            seatInfo.setConcertId(seat.getConcertId());
            seatInfo.setScheduleId(seat.getScheduleId());
            seatInfo.setSeatNo(seat.getSeatNo());
            seatInfo.setState(seat.getState());
            seatInfo.setGrade(seat.getGrade());
            seatInfo.setPrice(seat.getPrice());
            this.seats.add(seatInfo);
        });
    }
}

package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.Concert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ConcertResponseDto {


    private List<ConcertInfo> concertList = new ArrayList<>();

    @Getter
    @Setter
    public static class ConcertInfo {
        private Integer concertId;
        private String concertName;

    }

    public ConcertResponseDto(List<Concert> concertList) {
        concertList.stream().forEach(item ->{
            ConcertInfo conInfo = new ConcertInfo();
            conInfo.setConcertId(item.getId());
            conInfo.setConcertName(item.getName());
            this.concertList.add(conInfo);
        });
    }
}

package com.example.concert_reservation.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer concertId;
    private LocalDateTime date;

    @Builder
    public Schedule(Integer id, Integer concertId, LocalDateTime date) {
        this.id = id;
        this.concertId = concertId;
        this.date = date;
    }
}

package com.example.concert_reservation.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private long amount;

    @Version
    private int version;

    public Point() {

    }

    public Point(Integer userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }


}

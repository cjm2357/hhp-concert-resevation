package com.example.concert_reservation.fixture;

import com.example.concert_reservation.domain.entity.Concert;

public class ConcertFixture {
    public static Concert createConcert(Integer id, String name) {
        Concert concert = Concert.builder()
                .id(id)
                .name(name)
                .build();
        return concert;
    }
}

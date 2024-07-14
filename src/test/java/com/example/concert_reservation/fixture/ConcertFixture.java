package com.example.concert_reservation.fixture;

import com.example.concert_reservation.entity.Concert;

public class ConcertFixture {
    public static Concert createConcert(Integer id, String name) {
        Concert concert = new Concert();
        concert.setId(id);
        concert.setName(name);
        return concert;
    }
}

package com.example.concert_reservation.domain.service.repository;

import com.example.concert_reservation.domain.entity.Concert;

import java.util.List;


public interface ConcertRepository {


    List<Concert> findAll();

    Concert save(Concert concert);

}

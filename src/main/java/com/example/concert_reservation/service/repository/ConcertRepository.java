package com.example.concert_reservation.service.repository;

import com.example.concert_reservation.entity.Concert;
import com.example.concert_reservation.entity.User;

import java.util.List;


public interface ConcertRepository {


    List<Concert> findAll();

    Concert save(Concert concert);

}

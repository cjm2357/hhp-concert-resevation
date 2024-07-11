package com.example.concert_reservation.repository.impl;

import com.example.concert_reservation.entity.Concert;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.repository.ConcertJpaRepository;
import com.example.concert_reservation.repository.UserJpaRepository;
import com.example.concert_reservation.service.repository.ConcertRepository;
import com.example.concert_reservation.service.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    public ConcertRepositoryImpl(ConcertJpaRepository concertJpaRepository) {
        this.concertJpaRepository = concertJpaRepository;
    }

    public List<Concert> findAll() {
        return concertJpaRepository.findAll();
    }

    public Concert save(Concert concert){return concertJpaRepository.save(concert);}
}

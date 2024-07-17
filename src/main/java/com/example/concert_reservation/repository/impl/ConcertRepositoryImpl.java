package com.example.concert_reservation.repository.impl;

import com.example.concert_reservation.domain.entity.Concert;
import com.example.concert_reservation.repository.ConcertJpaRepository;
import com.example.concert_reservation.domain.service.repository.ConcertRepository;
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

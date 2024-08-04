package com.example.concert_reservation.repository.impl;

import com.example.concert_reservation.domain.entity.Concert;
import com.example.concert_reservation.repository.ConcertCacheRepository;
import com.example.concert_reservation.repository.ConcertJpaRepository;
import com.example.concert_reservation.domain.service.repository.ConcertRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
@Slf4j
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertCacheRepository concertCacheRepository;

    public List<Concert> findAll() {
        List<Concert> concerts = concertCacheRepository.findConcerts();
        if (concerts == null || concerts.size() == 0) {
            concerts = concertJpaRepository.findAll();
            log.info("\033[34m read concerts from DB");
            //cache 적재
            concertCacheRepository.saveConcerts(concerts);
        }
        return concerts;
    }

    public Concert save(Concert concert){return concertJpaRepository.save(concert);}
}

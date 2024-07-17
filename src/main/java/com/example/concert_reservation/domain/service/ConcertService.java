package com.example.concert_reservation.domain.service;

import com.example.concert_reservation.domain.service.repository.ConcertRepository;
import com.example.concert_reservation.domain.entity.Concert;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService (ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> getConcertList() {
        return concertRepository.findAll();
    }

    public Concert saveConcert(Concert concert) {
        return concertRepository.save(concert);
    }
}

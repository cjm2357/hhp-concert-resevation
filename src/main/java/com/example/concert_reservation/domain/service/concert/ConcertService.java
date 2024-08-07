package com.example.concert_reservation.domain.service.concert;

import com.example.concert_reservation.domain.service.concert.ConcertRepository;
import com.example.concert_reservation.domain.entity.Concert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public List<Concert> getConcertList() {
        return concertRepository.findAll();
    }

}

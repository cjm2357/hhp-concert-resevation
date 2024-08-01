package com.example.concert_reservation.domain.service;

import com.example.concert_reservation.domain.service.repository.ConcertRepository;
import com.example.concert_reservation.domain.entity.Concert;
import com.example.concert_reservation.repository.ConcertCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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

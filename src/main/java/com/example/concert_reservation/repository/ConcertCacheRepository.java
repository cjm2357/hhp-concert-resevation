package com.example.concert_reservation.repository;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Concert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ConcertCacheRepository {

    public static final String HASH_KEY = "concerts";
    private final RedisTemplate redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    //hash로 저장되어있을때
    public List<Concert> findConcerts() {
        Map<String, Concert> concertMap = redisTemplate.opsForHash().entries(HASH_KEY);
        List<Concert> concerts = new ArrayList<>(concertMap.values());
        log.info("\033[34m read concerts from cache");
        return concerts;
    }

    // 콘서트가 업데이트시 hash로 추가 하여 리스트를 전체 업데이트 할필요 없게 만듬
    // hash로 저장
    public Concert saveConcert(Concert concert) {
        String key = Integer.toString(concert.getId());
        redisTemplate.opsForHash().put(HASH_KEY, key, stringifyConcert(concert));
        return concert;
    }

    // hash로 저장
    public List<Concert> saveConcerts(List<Concert> concerts) {
        Map<String, String> concertMap = concerts.stream().collect(Collectors.toMap(concert -> String.valueOf(concert.getId()), concert -> stringifyConcert(concert)));
        redisTemplate.opsForHash().putAll(HASH_KEY, concertMap);
        return concerts;
    }

    private Concert parseToConcert(String json) {
        try {
            return objectMapper.readValue(json, Concert.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert searched data to Concert Object");
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        }
    }

    private String stringifyConcert(Concert concert) {
        try {
            String jsonString = objectMapper.writeValueAsString(concert);
            return jsonString;
        } catch (JsonProcessingException e) {
            log.error("Failed to convert Concert Object to json string");
            throw new CustomException(CustomExceptionCode.INVALID_OBJECT_TYPE);
        }
    }

}

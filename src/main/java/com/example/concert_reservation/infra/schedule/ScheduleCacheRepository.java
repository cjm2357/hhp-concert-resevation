package com.example.concert_reservation.infra.schedule;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Schedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ScheduleCacheRepository {

    public static final String PRE_HASH_KEY = "schedules:";
    private final RedisTemplate redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();


    public List<Schedule> findSchedulesByConcertId(Integer concertId) {
        String hashKey = PRE_HASH_KEY + concertId;
        Map<String, Schedule> scheduleMap = redisTemplate.opsForHash().entries(hashKey);

        List<Schedule> schedules = scheduleMap.entrySet().stream()
                .map(entry -> {
                    Object value = entry.getValue();
                    return parseToSchedule((String)value);
                })
                .collect(Collectors.toList());
        log.info("\033[34m read schedules from cache");
        return schedules;
    }

    public Schedule saveSchedule(Schedule schedule) {
        String hashKey = PRE_HASH_KEY + schedule.getConcertId();
        String key = Integer.toString(schedule.getId());
        redisTemplate.opsForHash().put(hashKey, key, stringifySchedule(schedule));
        return schedule;
    }

    public List<Schedule> saveSchedules(List<Schedule> schedules) {
        String hashKey = PRE_HASH_KEY + schedules.get(0).getConcertId();
        Map<String, String> scheduleMap = schedules.stream().collect(Collectors.toMap(schedule -> String.valueOf(schedule.getId()), schedule -> stringifySchedule(schedule)));
        redisTemplate.opsForHash().putAll(hashKey, scheduleMap);
        return schedules;
    }

    private Schedule parseToSchedule(String json) {
        try {
            return objectMapper.readValue(json, Schedule.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert searched data to Schedule Object");
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        }
    }

    public String stringifySchedule(Schedule schedule) {
        try {
            String jsonString = objectMapper.writeValueAsString(schedule);
            return jsonString;
        } catch (JsonProcessingException e) {
            log.error("Failed to convert Schedule Object to json string");
            throw new CustomException(CustomExceptionCode.INVALID_OBJECT_TYPE);
        }
    }
}

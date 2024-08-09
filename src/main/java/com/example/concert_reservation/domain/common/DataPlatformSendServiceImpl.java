package com.example.concert_reservation.domain.common;

import com.example.concert_reservation.domain.entity.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataPlatformSendServiceImpl implements DataPlatformSendService {

    @Override
    public void sendMessage(String text, Reservation reservation) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error("fail send Message");
        }
        log.info("concert : {}, schedule : {}, seat : {} is {}.", reservation.getConcertId(),reservation.getScheduleId(), reservation.getSeatId(), text);
    }
}

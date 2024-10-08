package com.example.concert_reservation.infra.dataPlatform;

import com.example.concert_reservation.domain.common.DataPlatform;
import com.example.concert_reservation.domain.entity.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class DataPlatformImpl implements DataPlatform {

    @Override
    public void sendMessage(String text, Reservation reservation) {
        try {
            Thread.sleep(1000);
            log.info("concert : {}, schedule : {}, seat : {} is {}.", reservation.getConcertId(),reservation.getScheduleId(), reservation.getSeatId(), text);
        } catch (Exception e) {
            log.error("fail send Message");
        }
    }
}

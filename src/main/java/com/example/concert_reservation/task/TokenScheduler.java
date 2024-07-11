package com.example.concert_reservation.task;

import com.example.concert_reservation.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenScheduler {

    @Autowired
    TokenService tokenService;

    //30초마다 작동
    @Scheduled(fixedRate = 30 * 1000)
    public void expireToken() {
        tokenService.expireToken();
    }

}

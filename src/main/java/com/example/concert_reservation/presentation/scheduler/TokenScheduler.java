package com.example.concert_reservation.presentation.scheduler;

import com.example.concert_reservation.application.TokenFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenScheduler {

    @Autowired
    TokenFacade tokenFacade;

    //30초마다 작동
    @Scheduled(fixedRate = 30 * 1000)
    public void expireToken() {
        tokenFacade.expireToken();
    }

}
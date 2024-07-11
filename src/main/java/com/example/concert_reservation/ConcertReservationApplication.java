package com.example.concert_reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConcertReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcertReservationApplication.class, args);
    }

}

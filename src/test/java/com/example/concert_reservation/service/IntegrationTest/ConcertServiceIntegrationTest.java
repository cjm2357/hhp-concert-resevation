package com.example.concert_reservation.service.IntegrationTest;


import com.example.concert_reservation.entity.Concert;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.fixture.ConcertFixture;
import com.example.concert_reservation.service.ConcertService;
import com.example.concert_reservation.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class ConcertServiceIntegrationTest {


    @Autowired
    ConcertService concertService;

    @Autowired
    UserRepository userRepository;

    @Test
    void 콘서트조회_성공 () {
        //given
        Concert concert1 = ConcertFixture.createConcert(null,"A concert");
        Concert concert2 = ConcertFixture.createConcert(null,"B concert");
        concertService.saveConcert(concert1);
        concertService.saveConcert(concert2);

        //when
        List<Concert> concerts = concertService.getConcertList();

        //then
        assertEquals(2, concerts.size());
    }

    @Test
    void 콘서트조회_결과_없음 () {


        //when
        List<Concert> concerts = concertService.getConcertList();

        //then
        assertEquals(0, concerts.size());
    }


}

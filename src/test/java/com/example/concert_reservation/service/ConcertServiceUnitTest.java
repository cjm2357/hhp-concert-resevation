package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Concert;
import com.example.concert_reservation.service.repository.ConcertRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConcertServiceUnitTest {

    @Mock
    ConcertRepository concertRepository;

    @InjectMocks
    ConcertService concertService;

    @Test
    void 콘서트리스트_조회_성공() {
        //given
        List<Concert> expectedConcertList = new ArrayList<>();
        Concert concert1 = new Concert();
        concert1.setId(1);
        concert1.setName("아이유 콘서트");
        expectedConcertList.add(concert1);

        Concert concert2 = new Concert();
        concert2.setId(2);
        concert2.setName("박효신 콘서트");
        expectedConcertList.add(concert2);

        when(concertRepository.findAll()).thenReturn(expectedConcertList);

        //when
        List<Concert> concertList = concertService.getConcertList();

        //then
        assertEquals(concertList, expectedConcertList);
    }

    @Test
    void 콘서트리스트_조회_결과_없음() {
        //given
        List<Concert> expectedConcertList = new ArrayList<>();


        when(concertRepository.findAll()).thenReturn(expectedConcertList);

        //when
        List<Concert> concertList = concertService.getConcertList();

        //then
        assertEquals(0, concertList.size());
    }
}

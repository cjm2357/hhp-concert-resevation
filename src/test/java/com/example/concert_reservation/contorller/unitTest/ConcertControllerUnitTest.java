package com.example.concert_reservation.contorller.unitTest;


import com.example.concert_reservation.controller.ConcertController;
import com.example.concert_reservation.entity.Concert;
import com.example.concert_reservation.fixture.ConcertFixture;
import com.example.concert_reservation.service.ConcertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConcertController.class)
@ExtendWith(MockitoExtension.class)
public class ConcertControllerUnitTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ConcertService concertService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 콘서트리스트_조회_성공 () throws Exception {
        //given
        List<Concert> concertList = new ArrayList<>();
        Concert concert1 = ConcertFixture.createConcert(1, "아이유 콘서트");
        Concert concert2 = ConcertFixture.createConcert(2, "박효신 콘서트");

        concertList.add(concert1);
        concertList.add(concert2);

        when(concertService.getConcertList()).thenReturn(concertList);
        //when
        //then
        mvc.perform(get("/api/concerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concertList.size()").value(2));
    }


}

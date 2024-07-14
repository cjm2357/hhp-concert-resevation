package com.example.concert_reservation.contorller.unitTest;

import com.example.concert_reservation.controller.SeatController;
import com.example.concert_reservation.dto.SeatReservationRequestDto;
import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.manager.TokenManager;
import com.example.concert_reservation.service.SeatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeatController.class)
@ExtendWith(MockitoExtension.class)
public class SeatControllerUnitTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    SeatService seatService;

    @MockBean
    TokenManager tokenManager;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 예약가능시트_조회_성공() throws Exception {
        //given
        Integer scheduleId = 1;

        List<Seat> expectedSeats = new ArrayList<>();
        Seat seat1 = new Seat();
        seat1.setId(1);
        seat1.setConcertId(1);
        seat1.setScheduleId(scheduleId);
        seat1.setSeatNo(1);
        seat1.setState(Seat.State.EMPTY);
        Seat seat2 = new Seat();
        seat2.setId(2);
        seat2.setConcertId(1);
        seat2.setScheduleId(scheduleId);
        seat2.setSeatNo(2);
        seat2.setState(Seat.State.EMPTY);
        expectedSeats.add(seat1);
        expectedSeats.add(seat2);

        UUID tokenKey = UUID.randomUUID();


        when(seatService.getAvailableSeatList(any())).thenReturn(expectedSeats);

        //when
        //then
        mvc.perform(get("/api/schedules/" + scheduleId+ "/seats" )
                        .header("Authorization", tokenKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seats.size()").value(2));
    }

    //조회 실패
    @Test
    void 예약가능시트_조회_실패() throws Exception{
        //given
        Integer scheduleId = -1;
        UUID tokenKey = UUID.randomUUID();

        //when
        //then
        mvc.perform(get("/api/schedules/" + scheduleId + "/seats")
                .header("Authorization", tokenKey))
                .andExpect(status().isBadRequest());
    }


    @Test
    void 토큰없이_조회() throws Exception{
        //given
        Integer scheduleId = 1;


        //when
        //then
        mvc.perform(get("/api/schedules/" + scheduleId+ "/seats"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    void 좌석예약_성공() throws Exception {
        //given
        UUID tokenKey = UUID.randomUUID();

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setSeatId(1);
        reservation.setScheduleId(1);
        reservation.setSeatNo(1);
        reservation.setConcertId(1);
        reservation.setCreatedTime(LocalDateTime.now());
        reservation.setExpiredTime(LocalDateTime.now().plusMinutes(Reservation.EXPIRE_TIME_FIVE_MIN));

        SeatReservationRequestDto requestDto = new SeatReservationRequestDto();
        requestDto.setUserId(1);

        when(seatService.reserveSeat(any())).thenReturn(reservation);
        //when

        //then
        mvc.perform(post("/api/seats/" + 1 + "/reservation")
                .header("Authorization", tokenKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }


    @Test
    void 좌석예약_실패() throws Exception {
        //given

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setSeatId(1);
        reservation.setScheduleId(1);
        reservation.setSeatNo(1);
        reservation.setConcertId(1);
        reservation.setCreatedTime(LocalDateTime.now());
        reservation.setExpiredTime(LocalDateTime.now().plusMinutes(Reservation.EXPIRE_TIME_FIVE_MIN));

        SeatReservationRequestDto requestDto = new SeatReservationRequestDto();
        requestDto.setUserId(1);

        when(seatService.reserveSeat(any())).thenReturn(reservation);
        //when

        //then
        mvc.perform(post("/api/seats/" + 1 + "/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }



}

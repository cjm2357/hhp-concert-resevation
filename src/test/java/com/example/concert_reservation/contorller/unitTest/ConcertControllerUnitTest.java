package com.example.concert_reservation.contorller.unitTest;


import com.example.concert_reservation.application.ConcertFacade;
import com.example.concert_reservation.controller.ConcertController;
import com.example.concert_reservation.dto.PaymentRequestDto;
import com.example.concert_reservation.dto.SeatReservationRequestDto;
import com.example.concert_reservation.entity.*;
import com.example.concert_reservation.fixture.ConcertFixture;
import com.example.concert_reservation.fixture.PaymentFixture;
import com.example.concert_reservation.fixture.ScheduleFixture;
import com.example.concert_reservation.fixture.SeatFixture;
import com.example.concert_reservation.interceptor.TokenInterceptor;
import com.example.concert_reservation.manager.TokenManager;
import com.example.concert_reservation.service.ConcertService;
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

@WebMvcTest(ConcertController.class)
@ExtendWith(MockitoExtension.class)
public class ConcertControllerUnitTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ConcertFacade concertFacade;

    @MockBean
    TokenInterceptor tokenInterceptor;

    @MockBean
    TokenManager tokenManager;

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

        when(concertFacade.getConcertList()).thenReturn(concertList);
        //when
        //then
        mvc.perform(get("/api/concerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concertList.size()").value(2));
    }

    @Test
    void 예약가능일정_조회_성공() throws Exception {
        //given
        UUID tokenKey = UUID.randomUUID();
        List<Schedule> expectedSchedules = new ArrayList<>();
        Schedule schedule1 = ScheduleFixture.createSchedule(1, 1, LocalDateTime.of(2024, 1, 1, 0, 0));
        expectedSchedules.add(schedule1);

        Schedule schedule2 = ScheduleFixture.createSchedule(2, 1,LocalDateTime.of(2024, 1,3,0,0));
        expectedSchedules.add(schedule2);

        when(concertFacade.getAvailableScheduleList(any())).thenReturn(expectedSchedules);

        //when
        //then
        mvc.perform(get("/api/concerts/"+ 1 +"/schedules")
                .header("Authorization", tokenKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedulesList.size()").value(2));
    }

    //조회 실패
    @Test
    void 예약가능일정_조회_실패() throws Exception{
        //given
        Integer concertId = -1;

        //when
        //then
        mvc.perform(get("/api/concerts/"+ concertId + "/schedules"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 예약가능시트_조회_성공() throws Exception {
        //given
        Integer scheduleId = 1;

        List<Seat> expectedSeats = new ArrayList<>();
        Seat seat1 = SeatFixture.createSeat(1, 1, scheduleId, 1, Seat.State.EMPTY, 10000l, "A");
        Seat seat2 = SeatFixture.createSeat(1, 1, scheduleId, 2, Seat.State.EMPTY, 10000l, "A");
        expectedSeats.add(seat1);
        expectedSeats.add(seat2);

        UUID tokenKey = UUID.randomUUID();

        when(concertFacade.getAvailableSeatList(any())).thenReturn(expectedSeats);

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

        when(concertFacade.reserveSeat(any(), any())).thenReturn(reservation);
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

        when(concertFacade.reserveSeat(any(), any())).thenReturn(reservation);
        //when

        //then
        mvc.perform(post("/api/seats/" + 1 + "/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void 결제_성공() throws Exception{
        //given
        UUID tokenKey = UUID.randomUUID();

        Integer userId = 1;
        Integer reservationId = 1;
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setUserId(userId);
        requestDto.setReservationId(reservationId);

        Payment payment = PaymentFixture.createPayment(1, userId, reservationId, LocalDateTime.now());

        when(concertFacade.pay(any())).thenReturn(payment);

        //when
        //then
        mvc.perform(post("/api/payment")
                .header("Authorization", tokenKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1))
                .andExpect(jsonPath("$.userId").value(userId));

    }

    @Test
    void 결제_실패_예약ID_없음() throws Exception{
        //given
        UUID tokenKey = UUID.randomUUID();

        Integer userId = 1;
        Integer reservationId = -1;
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setUserId(userId);
        requestDto.setReservationId(reservationId);

        Payment payment = PaymentFixture.createPayment(1, userId, reservationId, LocalDateTime.now());

        when(concertFacade.pay(any())).thenReturn(payment);

        //when
        //then
        mvc.perform(post("/api/payment")
                .header("Authorization", tokenKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("no reservation ID"));

    }

    @Test
    void 결제_실패_토큰없음() throws Exception{
        //given

        Integer userId = 1;
        Integer reservationId = -1;
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setUserId(userId);
        requestDto.setReservationId(reservationId);

        Payment payment = PaymentFixture.createPayment(1, userId, reservationId, LocalDateTime.now());

        when(concertFacade.pay(any())).thenReturn(payment);

        //when
        //then
        mvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("invalid token"));

    }


}

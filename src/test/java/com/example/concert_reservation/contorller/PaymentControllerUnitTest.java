package com.example.concert_reservation.contorller;

import com.example.concert_reservation.controller.PaymentController;
import com.example.concert_reservation.dto.PaymentRequestDto;
import com.example.concert_reservation.entity.Payment;
import com.example.concert_reservation.manager.TokenManager;
import com.example.concert_reservation.service.PaymentService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@ExtendWith(MockitoExtension.class)
public class PaymentControllerUnitTest {


    @Autowired
    MockMvc mvc;

    @MockBean
    PaymentService paymentService;

    @MockBean
    TokenManager tokenManager;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void 결제_성공() throws Exception{
        //given
        UUID tokenKey = UUID.randomUUID();

        Integer userId = 1;
        Integer reservationId = 1;
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setUserId(userId);
        requestDto.setReservationId(reservationId);

        Payment payment = new Payment();
        payment.setId(1);
        payment.setUserId(userId);
        payment.setReservationId(reservationId);
        payment.setCreatedTime(LocalDateTime.now());


        when(paymentService.pay(any())).thenReturn(payment);

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
    void 결제_실패_예약ID_없음ㅊ() throws Exception{
        //given
        UUID tokenKey = UUID.randomUUID();

        Integer userId = 1;
        Integer reservationId = -1;
        PaymentRequestDto requestDto = new PaymentRequestDto();
        requestDto.setUserId(userId);
        requestDto.setReservationId(reservationId);

        Payment payment = new Payment();
        payment.setId(1);
        payment.setUserId(userId);
        payment.setReservationId(reservationId);
        payment.setCreatedTime(LocalDateTime.now());


        when(paymentService.pay(any())).thenReturn(payment);

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

        Payment payment = new Payment();
        payment.setId(1);
        payment.setUserId(userId);
        payment.setReservationId(reservationId);
        payment.setCreatedTime(LocalDateTime.now());


        when(paymentService.pay(any())).thenReturn(payment);

        //when
        //then
        mvc.perform(post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("invalid token"));

    }

}

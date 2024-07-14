package com.example.concert_reservation.contorller.unitTest;

import com.example.concert_reservation.controller.UserPointController;
import com.example.concert_reservation.dto.UserPointRequestDto;
import com.example.concert_reservation.dto.UserPointResponseDto;
import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.UserPointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserPointController.class)
@ExtendWith(MockitoExtension.class)
public class UserPointControllerUnitTest {



    @Autowired
    MockMvc mvc;

    @MockBean
    UserPointService userPointService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 포인트 조회
     * 1.포인트 조회 성공
     * 2.유저 ID 없음
     * */
    @Test
    void 포인트조회_성공() throws Exception{
        //given
        UserPointRequestDto requestDto = new UserPointRequestDto();
        requestDto.setUserId(1);

        User user = new User();
        user.setId(1);
        Point point = new Point();
        point.setId(1);
        point.setUserId(user.getId());
        point.setAmount(1000l);
        user.setPoint(point);

        UserPointResponseDto responseDto = new UserPointResponseDto();
        responseDto.setUserId(1);
        responseDto.setAmount(1000l);

        when(userPointService.getPoint(any())).thenReturn(user);

        //when
        //then
        mvc.perform(post("/api/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.amount").value(1000l));

    }

    @Test
    void 유저ID_없음() throws Exception{
        //given
        UserPointRequestDto requestDto = new UserPointRequestDto();

        //when
        //then
        mvc.perform(post("/api/points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("no user ID"));

    }

    /**
     * 포인트 충전
     * 1.포인트 충전 성공
     * 2.bad_request
     * */
    @Test
    void 포인트충전_성공() throws Exception{
        //given
        UserPointResponseDto requestDto = new UserPointResponseDto();
        requestDto.setUserId(1);
        requestDto.setAmount(9000l);

        User user = new User();
        user.setId(1);
        Point point = new Point();
        point.setId(1);
        point.setUserId(1);
        point.setAmount(11000l);
        user.setPoint(point);

        when(userPointService.chargePoint(any(), any())).thenReturn(user);

        //when
        //then
        mvc.perform(post("/api/points/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.amount").value(11000l));

    }

    @Test
    void badRequest() throws Exception{
        //given
        UserPointRequestDto requestDto = new UserPointRequestDto();

        //when
        //then
        mvc.perform(post("/api/points/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("bad request"));

    }
}

package com.example.concert_reservation.contorller.unitTest;

import com.example.concert_reservation.controller.ScheduleController;
import com.example.concert_reservation.entity.Schedule;
import com.example.concert_reservation.manager.TokenManager;
import com.example.concert_reservation.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@ExtendWith(MockitoExtension.class)
public class ScheduleControllerUnitTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ScheduleService scheduleService;

    @MockBean
    TokenManager tokenManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 예약가능일정_조회_성공() throws Exception {
        //given
        UUID tokenKey = UUID.randomUUID();
        List<Schedule> expectedSchedules = new ArrayList<>();
        Schedule schedule1 = new Schedule();
        schedule1.setId(1);
        schedule1.setConcertId(1);
        schedule1.setDate(LocalDateTime.of(2024, 1, 1, 0, 0));
        expectedSchedules.add(schedule1);

        Schedule schedule2 = new Schedule();
        schedule2.setId(2);
        schedule2.setConcertId(1);
        schedule2.setDate(LocalDateTime.of(2024, 1,3,0,0));
        expectedSchedules.add(schedule2);

        when(scheduleService.getAvailableScheduleList(any())).thenReturn(expectedSchedules);

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



}

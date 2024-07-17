package com.example.concert_reservation.contorller.unitTest;

import com.example.concert_reservation.application.TokenFacade;
import com.example.concert_reservation.presentation.controller.TokenController;
import com.example.concert_reservation.dto.TokenRequestDto;
import com.example.concert_reservation.domain.entity.Token;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.fixture.TokenFixture;
import com.example.concert_reservation.presentation.interceptor.TokenInterceptor;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenController.class)
@ExtendWith(MockitoExtension.class)
public class TokenControllerUnitTest {


    @Autowired
    MockMvc mvc;

    @MockBean
    TokenFacade tokenFacade;

    @MockBean
    TokenInterceptor tokenInterceptor;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 토큰 발급 API
     * 1. 발급 성공
     * 2. userId가 없을 경우
     * 3. 이미 발급된 유저인 경우
     * */
    @Test
    void 토큰발급_성공() throws Exception{
        //given
        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setUserId(1);

        UUID tokenKey = UUID.randomUUID();
        User user = new User();
        user.setId(1);
        Token token = TokenFixture.createToken(1, user, tokenKey, LocalDateTime.now(), Token.TokenState.ACTIVATE);
        token.setOrder(0);

        // JSON 문자열을 직접 생성
        when(tokenFacade.getToken(requestDto.getUserId())).thenReturn(token);

        //when
        //then
        mvc.perform(post("/api/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value(token.getTokenKey().toString()))
                .andExpect(jsonPath("$.order").value(0));

    }

    //userID없이 요청
    @Test
    void 유저ID없이_요청() throws Exception{
        //given
        TokenRequestDto requestDto = new TokenRequestDto();

        //when
        //then
        mvc.perform(post("/api/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("no user ID"));

    }


    /**
     * 토큰 상태 조회 API
     * 1. 토큰 상태 조회 성공
     * 2. 유효하지 않은 토큰
     * */
    @Test
    void 토큰상태조회_성공() throws Exception{
        //given
        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setUserId(1);

        UUID tokenKey = UUID.randomUUID();
        User user = new User();
        user.setId(1);
        Token token = TokenFixture.createToken(1, user, tokenKey, LocalDateTime.now(), Token.TokenState.WAITING);
        token.setOrder(100);
        token.setTokenKey(tokenKey);

        when(tokenFacade.getTokenStatusAndUpdate(tokenKey)).thenReturn(token);

        //when
        //then
        mvc.perform(get("/api/token/status")
                        .header("Authorization", tokenKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value(token.getTokenKey().toString()))
                .andExpect(jsonPath("$.order").value(100));

    }




}

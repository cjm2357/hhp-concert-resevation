package com.example.concert_reservation.facade.integrationTest;

import com.example.concert_reservation.application.UserPointFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.service.repository.PointRepository;
import com.example.concert_reservation.domain.service.repository.UserRepository;
import com.example.concert_reservation.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserPointFacadeIntegrationTest {

    @Autowired
    UserPointFacade userPointFacade;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointRepository pointRepository;

    @BeforeEach
    void init() {

        User user = UserFixture.createUser(1, "유저1", 1, 10000l);
        pointRepository.save(user.getPoint());
        userRepository.save(user);
    }


    @Test
    void 유저_포인트조회_성공 () {
        //given
        int userId = 1;

        //when
        User user = userPointFacade.getUserWithPoint(userId);

        //then
        assertEquals(1, user.getId());
        assertEquals(10000l, user.getPoint().getAmount());
    }

    @Test
    void 유저_포인트조회_실패_USER_NOT_FOUND() {
        //given
        int userId = 2;

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userPointFacade.getUserWithPoint(userId);
        });


        //then
        assertEquals(CustomExceptionCode.USER_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.USER_NOT_FOUND.getMessage().toString(), exception.getCustomExceptionCode().getMessage());
    }

    @Test
    void 포인트_충전_성공() {
        //given

        User user = UserFixture.createUser(2, "유저2", 2, 0l);
        pointRepository.save(user.getPoint());
        userRepository.save(user);

        long plusAmount = 10000l;

        //when
        user = userPointFacade.chargePoint(user, plusAmount);

        //then
        assertEquals(2, user.getId());
        assertEquals(10000, user.getPoint().getAmount());
    }


    @Test
    void 포인트_추가_충전_성공() {
        //given
        User user = userRepository.findById(1);
        long plusAmount = 10000;

        //when
        user = userPointFacade.chargePoint(user,plusAmount);

        //then
        assertEquals(1, user.getId());
        assertEquals(10000 + 10000, user.getPoint().getAmount());
    }


}

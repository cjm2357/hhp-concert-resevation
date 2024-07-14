package com.example.concert_reservation.service.IntegrationTest;


import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.service.UserPointService;
import com.example.concert_reservation.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserPointServiceIntegrationTest {

    @Autowired
    UserPointService userPointService;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void init() {
        User user = new User();
        user.setId(1);
        user.setName("유저1");
        userRepository.save(user);
    }
    
    @Test
    void 포인트_충전_성공() {
        //given

        User user = UserFixture.createUser(1, "유저1", 1, 10000l);

        //when
        user = userPointService.chargePoint(user, user.getPoint().getAmount());

        //then
        assertEquals(1, user.getId());
        assertEquals(10000, user.getPoint().getAmount());

    }

    @Test
    void 포인트_충전_실패() {
        //given
        User user = UserFixture.createUser(1, "유저1", 1, -10000l);

        //when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            userPointService.chargePoint(user, user.getPoint().getAmount());
        });

        //then
        assertEquals("충전하려는 값이 음수입니다.", exception.getMessage().toString());


    }
    
    @Test
    void 포인트_추가_충전() {
        //given
        User user = UserFixture.createUser(1, "유저1", 1, 10000l);
        userPointService.chargePoint(user, user.getPoint().getAmount());

        //when
        user = userPointService.chargePoint(user,10000l);

        //then
        assertEquals(1, user.getId());
        assertEquals(10000 + 10000, user.getPoint().getAmount());
    }

    @Test
    void 포인트_조회_성공() {
        //given
        User user = UserFixture.createUser(1, "유저1", 1, 10000l);
        userPointService.chargePoint(user, user.getPoint().getAmount());

        //when
        user = userPointService.getPoint(user);

        //then
        assertEquals(1, user.getId());
        assertEquals(10000, user.getPoint().getAmount());

    }

    @Test
    void 포인트_조회_실패() {
        //given
        User user = new User();
        user.setId(-1);

        //when
        Throwable exception = assertThrows(NullPointerException.class, () -> {
            userPointService.getPoint(user);
        });

        //then
        assertEquals("해당 유저의 정보가 없습니다.", exception.getMessage().toString());

    }
    
}

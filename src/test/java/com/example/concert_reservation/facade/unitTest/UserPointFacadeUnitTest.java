package com.example.concert_reservation.facade.unitTest;

import com.example.concert_reservation.application.UserPointFacade;
import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.domain.service.PointService;
import com.example.concert_reservation.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPointFacadeUnitTest {

    @Mock
    UserService userService;

    @Mock
    PointService pointService;

    @InjectMocks
    UserPointFacade userPointFacade;

    @Test
    void 포이튼조회_유저정보없음_실패() {
        //given
        User user = UserFixture.createUser(1, "user1", 1, 1000l);

        when(userService.getUser(any())).thenReturn(null);

        //when
        User responseUser = userPointFacade.getUserWithPoint(user.getId());

        //then
        assertEquals(null, responseUser);
    }

    @Test
    void 포인트조회_성공() {
        //given
        User user = UserFixture.createUser(1, "user1", 1, 1000l);

        when(userService.getUser(any())).thenReturn(user);

        //when
        User responseUser = userPointFacade.getUserWithPoint(user.getId());

        //then
        assertEquals(user.getId(), responseUser.getId());
        assertEquals(1000l, responseUser.getPoint().getAmount());
    }


    @Test
    void 포인트충전_성공() {
        //given
        User user = UserFixture.createUser(1, "user1", 1, 3000l);

        when(pointService.getPointByUserIdWithLock(any())).thenReturn(user.getPoint());

        Long plusPoint = 5000l;
        Point chargePoint = new Point();
        chargePoint.setAmount(user.getPoint().getAmount() + plusPoint);
        when(pointService.chargePoint(any())).thenReturn(chargePoint);

        when(userService.save(any())).thenReturn(user);

        //when
        User responseUser = userPointFacade.chargePoint(user, plusPoint);

        //then
        assertEquals(user.getId(), responseUser.getId());
        assertEquals(3000 + 5000, responseUser.getPoint().getAmount());
    }

}

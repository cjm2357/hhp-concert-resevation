package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.repository.PointRepository;
import com.example.concert_reservation.service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPointServiceUnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PointRepository pointRepository;

    @InjectMocks
    UserPointService userPointService;

    @Test
    void 포인트조회_성공() {
        //given
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        Point point = new Point();
        point.setId(1);
        point.setUserId(userId);
        point.setAmount(1000l);
        user.setPoint(point);

        when(userRepository.findById(any())).thenReturn(user);


        //when
        User responseUser = userPointService.getPoint(user);

        //then
        assertEquals(userId, responseUser.getId());
        assertEquals(1000l, responseUser.getPoint().getAmount());
    }

    @Test
    void 포인트조회_실패() {
        //given
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(any())).thenReturn(null);

        //when
        Throwable exception = assertThrows(NullPointerException.class, () -> {
            userPointService.getPoint(user);
        });


        //then
        assertEquals("해당 유저의 정보가 없습니다.", exception.getMessage().toString());
    }


    @Test
    void 포인트충전_성공() {
        //given
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        Point point = new Point();
        point.setId(1);
        point.setUserId(userId);
        point.setAmount(3000l);
        user.setPoint(point);

        Long plusPoint = 5000l;
        Point chargePoint = new Point();
        chargePoint.setId(1);
        chargePoint.setAmount(8000l);
        chargePoint.setUserId(userId);

        when(pointRepository.findByUserIdWithLock(any())).thenReturn(point);
        when(pointRepository.save(any())).thenReturn(chargePoint);


        //when
        User responseUser = userPointService.chargePoint(user, plusPoint);

        //then
        assertEquals(userId, responseUser.getId());
        assertEquals(3000 + 5000, responseUser.getPoint().getAmount());
    }

    @Test
    void 포인트충전_실패() {
        //given
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        Point point = new Point();
        point.setId(1);
        point.setUserId(userId);
        point.setAmount(-3000l);
        user.setPoint(point);

        //when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            userPointService.chargePoint(user, point.getAmount());
        });

        //then
        assertEquals("충전하려는 값이 음수입니다.", exception.getMessage().toString());

    }

}

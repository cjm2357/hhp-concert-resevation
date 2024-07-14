package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.service.UserPointService;
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
        User user = UserFixture.createUser(1, "user1", 1, 1000l);

        when(userRepository.findById(any())).thenReturn(user);

        //when
        User responseUser = userPointService.getPoint(user);

        //then
        assertEquals(user.getId(), responseUser.getId());
        assertEquals(1000l, responseUser.getPoint().getAmount());
    }

    @Test
    void 포인트조회_실패() {
        //given
        User user = UserFixture.createUser(1, "user1", null, null);

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
        User user = UserFixture.createUser(1, "user1", 1, 3000l);

        when(pointRepository.findByUserIdWithLock(any())).thenReturn(user.getPoint());

        Long plusPoint = 5000l;
        Point chargePoint = new Point();
        chargePoint.setAmount(user.getPoint().getAmount() + plusPoint);
        when(pointRepository.save(any())).thenReturn(chargePoint);

        when(userRepository.save(any())).thenReturn(user);

        //when
        User responseUser = userPointService.chargePoint(user, plusPoint);

        //then
        assertEquals(user.getId(), responseUser.getId());
        assertEquals(3000 + 5000, responseUser.getPoint().getAmount());
    }

    @Test
    void 포인트충전_실패() {
        //given
        User user = UserFixture.createUser(1, "user1", 1, -3000l);

        //when
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            userPointService.chargePoint(user, user.getPoint().getAmount());
        });

        //then
        assertEquals("충전하려는 값이 음수입니다.", exception.getMessage().toString());

    }

}

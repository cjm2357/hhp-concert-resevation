package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.service.PointService;
import com.example.concert_reservation.domain.service.repository.PointRepository;
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
public class PointServiceUnitTest {

    @Mock
    PointRepository pointRepository;

    @InjectMocks
    PointService pointService;

    @Test
    void 포인트_조회_성공() {
        //given
        Point expectedPoint = new Point();
        expectedPoint.setId(1);
        expectedPoint.setUserId(1);
        expectedPoint.setAmount(10000l);

        when(pointRepository.findByUserIdWithLock(expectedPoint.getUserId())).thenReturn(expectedPoint);
        //when
        Point point = pointService.getPointByUserIdWithLock(expectedPoint.getUserId());

        //then
        assertEquals(expectedPoint, point);
    }

    @Test
    void 포인트_조회_실패() {
        //given
        Point expectedPoint = new Point();
        expectedPoint.setId(1);
        expectedPoint.setUserId(1);
        expectedPoint.setAmount(10000l);

        when(pointRepository.findByUserIdWithLock(expectedPoint.getUserId())).thenReturn(null);
        //when

        CustomException exception = assertThrows(CustomException.class, () -> {
            pointService.getPointByUserIdWithLock(expectedPoint.getUserId());
        });

        //then
        assertEquals(CustomExceptionCode.USER_POINT_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.USER_POINT_NOT_FOUND.getMessage(), exception.getCustomExceptionCode().getMessage());

    }

    @Test
    void 포인트_저장_성공() {
        //given
        Point chargePoint = new Point();
        chargePoint.setId(1);
        chargePoint.setUserId(1);
        chargePoint.setAmount(10000l);

        when(pointRepository.save(any())).thenReturn(chargePoint);
        //when
        Point point = pointService.chargePoint(chargePoint);

        //then
        assertEquals(chargePoint, point);
    }
}

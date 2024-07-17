package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.service.PointService;
import com.example.concert_reservation.domain.service.repository.PointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}

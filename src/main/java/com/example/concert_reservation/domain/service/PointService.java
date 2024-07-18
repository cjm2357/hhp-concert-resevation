package com.example.concert_reservation.domain.service;


import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.service.repository.PointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PointService {

    private final PointRepository pointRepository;

    public PointService (PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Point chargePoint(Point point) {
        return pointRepository.save(point);
    }

    public Point getPointByUserIdWithLock(Integer userId) {
        Point point =  pointRepository.findByUserIdWithLock(userId);
        if (point == null) {
            log.warn("{} user point not found", userId);
            throw new CustomException(CustomExceptionCode.USER_POINT_NOT_FOUND);
        }
        return point;
    }
}
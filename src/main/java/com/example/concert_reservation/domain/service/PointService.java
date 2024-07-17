package com.example.concert_reservation.domain.service;


import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.service.repository.PointRepository;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService (PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Point chargePoint(Point point) {
        return pointRepository.save(point);
    }

    public Point getPointByUserIdWithLock(Integer userId) {
        return pointRepository.findByUserIdWithLock(userId);
    }
}

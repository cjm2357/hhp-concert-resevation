package com.example.concert_reservation.domain.point;


import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Point savePoint(User user, Long amount){
//        Point point = pointRepository.findByUserId(user.getId());
        Point point = pointRepository.findByUserIdWithLock(user.getId());
        point.setAmount(point.getAmount() + amount);
        point = pointRepository.save(point);
        return point;
    }

    @Transactional
    public Point payPoint(User user, Long amount){
        Point point = pointRepository.findByUserIdWithLock(user.getId());
        if (point.getAmount() < amount || point.getAmount() <= 0) {
            throw new CustomException(CustomExceptionCode.POINT_NOT_ENOUGH);
        }
        point.setAmount(point.getAmount() - amount);
        point = pointRepository.save(point);
        return point;
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

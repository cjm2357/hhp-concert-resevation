package com.example.concert_reservation.domain.service.repository;

import com.example.concert_reservation.domain.entity.Point;


public interface PointRepository {


    Point findByUserId(Integer userId);
    Point findByUserIdWithLock(Integer userId);


    Point save(Point point);


}

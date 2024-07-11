package com.example.concert_reservation.service.repository;

import com.example.concert_reservation.entity.Point;


public interface PointRepository {


    Point findByUserId(Integer userId);
    Point findByUserIdWithLock(Integer userId);


    Point save(Point point);


}

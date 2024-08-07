package com.example.concert_reservation.repository.point;


import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.repository.point.PointJpaRepository;
import com.example.concert_reservation.domain.service.point.PointRepository;
import org.springframework.stereotype.Repository;


@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    public PointRepositoryImpl (PointJpaRepository pointJpaRepository) {
        this.pointJpaRepository = pointJpaRepository;
    }

    @Override
    public Point findByUserId(Integer userId) {
        return pointJpaRepository.findByUserId(userId).orElse(new Point(userId, 0l));
    }
    @Override
    public Point findByUserIdWithLock(Integer userId) {
        return pointJpaRepository.findByUserIdWithLock(userId).orElse(new Point(userId, 0l));
    }
    @Override
    public Point save(Point point) {
       return pointJpaRepository.save(point);
    }
}

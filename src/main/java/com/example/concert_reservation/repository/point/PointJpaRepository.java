package com.example.concert_reservation.repository.point;

import com.example.concert_reservation.domain.entity.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p WHERE p.userId = :userId")
    Optional<Point> findByUserIdWithLock(Integer userId);
    Optional<Point> findByUserId(Integer userId);
}

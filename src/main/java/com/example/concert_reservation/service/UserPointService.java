package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.repository.PointRepository;
import com.example.concert_reservation.service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPointService {

    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    public UserPointService(UserRepository userRepository, PointRepository pointRepository) {
        this.userRepository = userRepository;
        this.pointRepository = pointRepository;
    }

    public User getPoint(User user) {
        User response = userRepository.findById(user.getId());
        if (response == null) {
            throw new NullPointerException("해당 유저의 정보가 없습니다.");
        }
        return response;

    }

    @Transactional
    public User chargePoint(User user, Long amount) {
        if (amount < 0 ) throw new IllegalArgumentException("충전하려는 값이 음수입니다.");
        Point point = pointRepository.findByUserIdWithLock(user.getId());
        point.setAmount(point.getAmount() + amount);
        point = pointRepository.save(point);
        user.setPoint(point);
        user = userRepository.save(user);
        return user;
    }

}

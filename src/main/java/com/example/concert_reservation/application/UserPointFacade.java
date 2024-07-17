package com.example.concert_reservation.application;

import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.PointService;
import com.example.concert_reservation.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserPointFacade {

    private final UserService userService;
    private final PointService pointService;

    public UserPointFacade (UserService userService, PointService pointService) {
        this.userService = userService;
        this.pointService = pointService;
    }

    public User getUserWithPoint(Integer userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NullPointerException("해당 유저의 정보가 없습니다.");
        }
        return user;
    }

    @Transactional
    public User chargePoint(User user, Long amount) {
        if (amount < 0 ) throw new IllegalArgumentException("충전하려는 값이 음수입니다.");
//        User userInfo = userService.getUser(user.getId());
        Point point = pointService.getPointByUserIdWithLock(user.getId());
        point.setAmount(point.getAmount() + amount);
        point = pointService.chargePoint(point);
        user.setPoint(point);
        user = userService.save(user);
        return user;
    }


}

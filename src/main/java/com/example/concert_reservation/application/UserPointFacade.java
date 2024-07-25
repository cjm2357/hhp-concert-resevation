package com.example.concert_reservation.application;

import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.service.PointService;
import com.example.concert_reservation.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
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
        return user;
    }

    public User chargePoint(User user, Long amount) {
        Point point = pointService.savePoint(user, amount);
        user.setPoint(point);
        log.info("{} user charge {} point", user.getId(), amount);

        return user;
    }


}

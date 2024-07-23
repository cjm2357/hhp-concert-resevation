package com.example.concert_reservation.application;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.service.PointService;
import com.example.concert_reservation.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    @Transactional
    public User chargePoint(User user, Long amount) {

        Point point = pointService.getPointByUserIdWithLock(user.getId());
        point.setAmount(point.getAmount() + amount);
        point = pointService.chargePoint(point);
        user.setPoint(point);
        log.info("{} user charge {} point", user.getId(), amount);

        return user;
    }


}

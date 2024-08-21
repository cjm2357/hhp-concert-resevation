package com.example.concert_reservation.application.user;

import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.point.PointService;
import com.example.concert_reservation.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Component
public class UserPointFacade {

    private final UserService userService;
    private final PointService pointService;
    private final RedissonClient redissonClient;


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

    public void usePoint(Integer userId, Long amount) {
        User user = new User();
        user.setId(userId);
        pointService.payPoint(user, amount);
        log.info("{} user pay {} point", user.getId(), amount);
    }



}

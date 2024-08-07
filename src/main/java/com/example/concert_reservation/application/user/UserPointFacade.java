package com.example.concert_reservation.application.user;

import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.service.point.PointService;
import com.example.concert_reservation.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;


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


//    public User chargePoint(User user, Long amount) {
//        String key = user.getPoint().getId()+ ":charge";
//        RLock rLock = redissonClient.getLock(key); // (1)
//
//
//        try {
//            boolean available = rLock.tryLock(5, 3, TimeUnit.SECONDS); // (2)
//            if (!available) {    // (3)
//                throw new RuntimeException("Lock 이용불가");
//            }
//            // 락 획득 후 수행 로직...
//            Point point = pointService.savePoint(user, amount);
//            user.setPoint(point);
//            log.info("{} user charge {} point", user.getId(), amount);
//            return user;
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Lock획득 실패");
//        } finally {
//            rLock.unlock(); // (4)
//        }
//
//        log.info("{} user charge {} point", user.getId(), amount);
//
//        return user;
//    }


}

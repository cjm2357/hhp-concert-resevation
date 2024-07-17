package com.example.concert_reservation.fixture;

import com.example.concert_reservation.domain.entity.Point;
import com.example.concert_reservation.domain.entity.User;

public class UserFixture {
    public static User createUser(Integer userId, String userName, Integer pointId, Long amount) {

        Point point = new Point();
        point.setId(pointId);
        point.setUserId(userId);
        point.setAmount(amount);
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        user.setPoint(point);

        return user;
    }
}

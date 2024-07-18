package com.example.concert_reservation.domain.service.repository;

import com.example.concert_reservation.domain.entity.User;


public interface UserRepository {

    User findById(Integer userId);

    User save(User user);
}

package com.example.concert_reservation.repository.impl;

import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.repository.UserJpaRepository;
import com.example.concert_reservation.domain.service.repository.UserRepository;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl (UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public User findById (Integer userId) {
        return userJpaRepository.findById(userId).orElse(null);
    }

    public User save (User user) {
        return userJpaRepository.save(user);
    }
}

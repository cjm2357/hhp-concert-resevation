package com.example.concert_reservation.service.repository;

import com.example.concert_reservation.entity.Token;
import com.example.concert_reservation.entity.User;

import java.util.UUID;


public interface UserRepository {

    User findById(User user);

    User save(User user);
}
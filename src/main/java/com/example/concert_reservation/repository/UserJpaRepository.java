package com.example.concert_reservation.repository;

import com.example.concert_reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

}

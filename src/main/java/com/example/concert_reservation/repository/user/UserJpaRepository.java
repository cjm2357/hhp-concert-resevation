package com.example.concert_reservation.repository.user;

import com.example.concert_reservation.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

}

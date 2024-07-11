package com.example.concert_reservation.repository.impl;

import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.repository.UserJpaRepository;
import com.example.concert_reservation.service.repository.UserRepository;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository userJpaRepository;

  public UserRepositoryImpl (UserJpaRepository userJpaRepository) {
      this.userJpaRepository = userJpaRepository;
  }

  public User findById (User user) {
      return userJpaRepository.findById(user.getId()).orElse(null);
  }

   public User save (User user) {
       return userJpaRepository.save(user);
   }
}
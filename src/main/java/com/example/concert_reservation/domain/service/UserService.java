package com.example.concert_reservation.domain.service;


import com.example.concert_reservation.domain.service.repository.UserRepository;
import com.example.concert_reservation.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Integer userId) {
        return userRepository.findById(userId);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}

package com.example.concert_reservation.domain.service.user;


import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.service.user.UserRepository;
import com.example.concert_reservation.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Integer userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            log.warn("{} user id not found", userId);
            throw new CustomException(CustomExceptionCode.USER_NOT_FOUND);
        }
            return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}

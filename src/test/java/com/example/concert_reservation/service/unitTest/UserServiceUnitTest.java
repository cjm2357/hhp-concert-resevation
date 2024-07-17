package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.domain.service.UserService;
import com.example.concert_reservation.domain.service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void 유저_조회_성공() {
        //given
        User expectedUser = UserFixture.createUser(1, "user1`", 1, 10000l);
        when(userRepository.findById((any()))).thenReturn(expectedUser);

        //when
        User user = userService.getUser(expectedUser.getId());

        //then
        assertEquals(expectedUser, user);
    }
    
    @Test
    void 유저_조회_실패() {
        User expectedUser = UserFixture.createUser(1, "user1`", 1, 10000l);
        when(userRepository.findById((any()))).thenReturn(expectedUser);

        //when
        User user = userService.getUser(expectedUser.getId());

        //then
        assertEquals(expectedUser, user);
    }

    @Test
    void 유저_저장_성공() {
        //given
        User saveUserInfo = UserFixture.createUser(1, "user1", 1, 10000l);
        when(userRepository.save(any())).thenReturn(saveUserInfo);

        //when
        User user = userService.save(saveUserInfo);
        //then
        assertEquals(saveUserInfo, user);
    }
}

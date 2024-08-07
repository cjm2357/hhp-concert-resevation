package com.example.concert_reservation.facade.integrationTest;

import com.example.concert_reservation.application.user.UserPointFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.service.point.PointRepository;
import com.example.concert_reservation.domain.service.user.UserRepository;
import com.example.concert_reservation.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
//@Transactional
public class UserPointFacadeIntegrationTest {

    @Autowired
    UserPointFacade userPointFacade;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointRepository pointRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserPointFacadeIntegrationTest.class);

    @BeforeEach
    void init() {

        User user = UserFixture.createUser(1, "유저1", 1, 10000l);
        pointRepository.save(user.getPoint());
        userRepository.save(user);
    }


    @Test
    void 유저_포인트조회_성공 () {
        //given
        int userId = 1;

        //when
        User user = userPointFacade.getUserWithPoint(userId);

        //then
        assertEquals(1, user.getId());
        assertEquals(10000l, user.getPoint().getAmount());
    }

    @Test
    void 유저_포인트조회_실패_USER_NOT_FOUND() {
        //given
        int userId = 2;

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            userPointFacade.getUserWithPoint(userId);
        });

        //then
        assertEquals(CustomExceptionCode.USER_NOT_FOUND.getStatus(), exception.getCustomExceptionCode().getStatus());
        assertEquals(CustomExceptionCode.USER_NOT_FOUND.getMessage().toString(), exception.getCustomExceptionCode().getMessage());
    }

    @Test
    void 포인트_충전_성공() {
        //given

        User user = UserFixture.createUser(2, "유저2", 2, 0l);
        pointRepository.save(user.getPoint());
        userRepository.save(user);

        long plusAmount = 10000l;

        //when
        user = userPointFacade.chargePoint(user, plusAmount);

        //then
        assertEquals(2, user.getId());
        assertEquals(10000, user.getPoint().getAmount());
    }


    @Test
    void 포인트_추가_충전_성공() {
        //given
        User user = userRepository.findById(1);
        long plusAmount = 10000;

        //when
        user = userPointFacade.chargePoint(user,plusAmount);

        //then
        assertEquals(1, user.getId());
        assertEquals(10000 + 10000, user.getPoint().getAmount());
    }

    @Test
    public void 포인트_충전_동시성_테스트 () throws Exception {
        //given
        int threadCount = 100;
        //user id : 1, point : 10000;
        User user = userRepository.findById(1);
        long point = 1000l;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // countdown을  설정
        List<Thread> workers = Stream.generate(() -> new Thread(new PointChargeRunner(user, point, countDownLatch)))
                .limit(threadCount) // 쓰레드를  생성
                .collect(Collectors.toList());

        //when
        long startTime = System.currentTimeMillis();
        workers.forEach(Thread::start); // 모든 쓰레드 시작
        countDownLatch.await(); // countdown이 0이 될때까지 대기한다는 의미
        long endTime = System.currentTimeMillis();
        logger.info("포인트 충전 동시성테스트 100번 run time : {}", endTime - startTime);

        User result = userRepository.findById(1);
        logger.info("user point :: {}", result.getPoint().getAmount());
        assertEquals(10000 + (1000 * threadCount), user.getPoint().getAmount());

    }



    private class PointChargeRunner implements Runnable {
        private CountDownLatch countDownLatch;
        private User user;
        private Long point;

        public PointChargeRunner(User user, long point, CountDownLatch countDownLatch) {
            this.user = user;
            this.point = point;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                userPointFacade.chargePoint(user, point);
            } catch (Exception e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }
    }


}

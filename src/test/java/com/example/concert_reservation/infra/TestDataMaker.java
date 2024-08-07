package com.example.concert_reservation.infra;

import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.entity.Schedule;
import com.example.concert_reservation.domain.entity.Seat;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.service.point.PointRepository;
import com.example.concert_reservation.domain.service.reservation.ReservationRepository;
import com.example.concert_reservation.domain.service.schedule.ScheduleRepository;
import com.example.concert_reservation.domain.service.seat.SeatRepository;
import com.example.concert_reservation.domain.service.user.UserRepository;
import com.example.concert_reservation.fixture.ReservationFixture;
import com.example.concert_reservation.fixture.ScheduleFixture;
import com.example.concert_reservation.fixture.SeatFixture;
import com.example.concert_reservation.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@SpringBootTest
public class TestDataMaker {

    @Autowired
    PointRepository pointRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ReservationRepository reservationRepository;

    private static final Logger logger = LoggerFactory.getLogger(TestDataMaker.class);



    @Test
    void makeUserAndPointQuery1() {
        int num = 250000;
        for (int i=1; i<= num; i++) {
            User user = UserFixture.createUser(i, "user"+i, i, i *10l);
            pointRepository.save(user.getPoint());
            userRepository.save(user);
        }

        int num2 = 500000;
        for (int i=num +1; i<= num2; i++) {
            User user = UserFixture.createUser(i, "user"+i, i, i*10l);
            pointRepository.save(user.getPoint());
            userRepository.save(user);
        }



    }

    @Test
    void makeScheduleQuery1() {
        int num = 250000;
        for (int i=1; i<= num; i++) {
            Schedule schedule = ScheduleFixture.createSchedule(i, (i%10) +1, LocalDateTime.of(2020 + i%12,(i %12) +1  ,(i%28) +1 , (i%12) +1,(i%59)+1));
            scheduleRepository.save(schedule);
        }

        int num2 = 500000;
        for (int i=num +1; i<= num2; i++) {
            Schedule schedule = ScheduleFixture.createSchedule(i, (i%10) +1, LocalDateTime.of(2020 + i%12,(i %12) +1 ,(i%28) +1 , (i%12) +1,(i%59)+1));
            scheduleRepository.save(schedule);
        }



    }

    @Test
    void makeSeatQuery1() {
        int num = 250000;
        Seat.State[] seatState = {Seat.State.RESERVED, Seat.State.EMPTY};
        Random random = new Random();
        String[] grades = {"A", "B", "C"};
        for (int i=1; i<= num; i++) {
            int stateIdx = random.nextInt(2);
            Seat seat = SeatFixture.createSeat(i, (i%10)+1,(i%1000)+1 ,(i%100)+1 ,seatState[stateIdx],i*10l , grades[(i%3)]);
            seatRepository.save(seat);
        }

        int num2 = 500000;
        for (int i=num +1; i<= num2; i++) {
            int stateIdx = random.nextInt(2);
            Seat seat = SeatFixture.createSeat(i, (i%10)+1,(i%1000)+1 ,(i%100)+1 ,seatState[stateIdx],i*10l , grades[(i%3)]);
            seatRepository.save(seat);
        }


    }


    @Test
    void makeReservationQuery1() {
        int num = 250000;
        Reservation.State[] reservationState = {Reservation.State.EXPIRED, Reservation.State.WAITING, Reservation.State.COMPLETED};
        Random random = new Random();
        String[] grades = {"A", "B", "C"};
        Set<String> reservedIdSet = new HashSet<>();
        for (int i=1; i<= num; i++) {
            int stateIdx = random.nextInt(2);
            if (stateIdx == 1 || stateIdx == 2) {
                if(reservedIdSet.contains(((i%1000)+1) + "_" + ((i%100) +1))) {
                    stateIdx = 0;
                } else {
                    reservedIdSet.add(((i%1000)+1) + "_" + ((i%100) +1));
                }
            }
            Reservation reservation =
                    ReservationFixture.creasteReservation(i, i, (i%10) +1, (i%150000) +1, (i%1000)+1, (i%100) +1, reservationState[stateIdx], i*10l,grades[i%3], LocalDateTime.of(2020 + i%12,(i %12) +1  ,(i%28) +1 , (i%12) +1,(i%59)+1) );
            reservationRepository.save(reservation);
        }

        int num2 = 500000;
        for (int i=num +1; i<= num2; i++) {
            int stateIdx = random.nextInt(2);
            if (stateIdx == 1 || stateIdx == 2) {
                if(reservedIdSet.contains(((i%1000)+1) + "_" + ((i%100) +1))) {
                    stateIdx = 0;
                } else {
                    reservedIdSet.add(((i%1000)+1) + "_" + ((i%100) +1));
                }
            }
            Reservation reservation =
                    ReservationFixture.creasteReservation(i, i, (i%10) +1, (i%150000) +1, (i%1000)+1, (i%100) +1, reservationState[stateIdx], i*10l,grades[i%3], LocalDateTime.of(2020 + i%12,(i %12) +1  ,(i%28) +1 , (i%12) +1,(i%59)+1) );
            reservationRepository.save(reservation);
        }


    }





}

//package com.example.concert_reservation.service.IntegrationTest;
//
//
//import com.example.concert_reservation.entity.Reservation;
//import com.example.concert_reservation.entity.Seat;
//import com.example.concert_reservation.entity.User;
//import com.example.concert_reservation.fixture.ReservationFixture;
//import com.example.concert_reservation.fixture.SeatFixture;
//import com.example.concert_reservation.service.SeatService;
//import com.example.concert_reservation.service.repository.ReservationRepository;
//import com.example.concert_reservation.service.repository.SeatRepository;
//import com.example.concert_reservation.service.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//
//@SpringBootTest
//@Transactional
//public class SeatServiceIntegrationTest {
//
//
//    @Autowired
//    SeatService seatService;
//
//    @Autowired
//    SeatRepository seatRepository;
//
//    @Autowired
//    ReservationRepository reservationRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @BeforeEach
//    void init() {
//        User user = new User();
//        user.setId(1);
//        user.setName("유저1");
//        userRepository.save(user);
//    }
//
//    @Test
//    void 예약가능_좌석_조회_있음 () {
//        //given
//        Integer scheduleId = 1;
//
//        Seat seat1 = SeatFixture.createSeat(null, 1, scheduleId, 1, Seat.State.RESERVED, 10000l, "A");
//        seat1 = seatRepository.save(seat1);
//
//        Seat seat2 = SeatFixture.createSeat(null, 1, scheduleId, 2, Seat.State.EMPTY, 10000l, "A");
//        seat2 = seatRepository.save(seat2);
//
//        Seat seat3 = SeatFixture.createSeat(null, 1, scheduleId, 3, Seat.State.EMPTY, 10000l, "A");
//        seat3 = seatRepository.save(seat3);
//
//
//        //when
//        List<Seat> seats = seatService.getAvailableSeatList(scheduleId);
//
//        //then
//        assertEquals(2, seats.size());
//        assertEquals(seat2.getSeatNo(), seats.get(0).getSeatNo());
//        assertEquals(seat3.getSeatNo(), seats.get(1).getSeatNo());
//
//    }
//
//    @Test
//    void 예약가능_좌석_조회_없음 () {
//        //given
//        Integer scheduleId = 1;
//
//
//        //when
//        List<Seat> seats = seatService.getAvailableSeatList(scheduleId);
//
//        //then
//        assertEquals(0, seats.size());
//
//    }
//
//    @Test
//    void 좌석_예약_성공() {
//
//        //given
//        Integer scheduleId = 1;
//        Integer userId = 1;
//        Integer concertId = 1;
//
//        Seat seat1 = SeatFixture.createSeat(null, concertId, scheduleId, 1, Seat.State.RESERVED, 1000l, "A");
//        seat1 = seatRepository.save(seat1);
//
//        Reservation reservation = new Reservation();
//        reservation.setUserId(userId);
//        reservation.setSeatId(seat1.getId());
//
//        //when
//        reservation = seatService.reserveSeat(reservation);
//
//        //then
//        Seat seat = seatRepository.findById(seat1.getId());
//        assertEquals(1, reservation.getConcertId());
//        assertEquals("A", reservation.getSeatGrade());
//        assertEquals(1000l, reservation.getPrice());
//        assertEquals(Reservation.State.WAITING, reservation.getState());
//        assertEquals(Seat.State.RESERVED, seat.getState());
//    }
//
//    @Test
//    void 좌석_예약_실패() {
//
//        //given
//        Integer scheduleId = 1;
//        Integer userId = 1;
//        Integer concertId = 1;
//
//
//        Seat seat1 =  SeatFixture.createSeat(null, concertId, scheduleId, 1, Seat.State.RESERVED, 1000l, "A");
//
//        seat1 = seatRepository.save(seat1);
//
//        Reservation prevReservation = new Reservation();
//        prevReservation.setUserId(2);
//        prevReservation.setSeatId(seat1.getId());
//
//        seatService.reserveSeat(prevReservation);
//
//        Reservation reservation = new Reservation();
//        reservation.setUserId(userId);
//        reservation.setSeatId(seat1.getId());
//
//
//        //when
//        Throwable exception = assertThrows(RuntimeException.class, () -> {
//            seatService.reserveSeat(reservation);
//        });
//
//        //then
//        assertEquals("이미 예약된 정보가 있습니다.", exception.getMessage().toString());
//
//    }
//
//    @Test
//    void 예약만료_좌석_상태_변경 () {
//        //given
//        Integer scheduleId = 1;
//        Integer concertId = 1;
//
//        Seat seat1 =  SeatFixture.createSeat(null, concertId, scheduleId, 1, Seat.State.RESERVED, 1000l, "A");
//        seat1 = seatRepository.save(seat1);
//
//        Seat seat2 =  SeatFixture.createSeat(null, concertId, scheduleId, 2, Seat.State.RESERVED, 1000l, "A");
//        seat2 = seatRepository.save(seat2);
//
//        Reservation reservation1 =
//                ReservationFixture.creasteReservation(null, 1, seat1.getConcertId(), seat1.getId(), seat1.getScheduleId(), seat1.getSeatNo(), Reservation.State.WAITING, seat1.getPrice(), seat1.getGrade(), LocalDateTime.now().minusMinutes(15));
//        reservation1 = reservationRepository.save(reservation1);
//
//        Reservation reservation2 =
//                ReservationFixture.creasteReservation(null, 2, seat2.getConcertId(), seat2.getId(), seat2.getScheduleId(), seat2.getSeatNo(), Reservation.State.WAITING, seat2.getPrice(), seat2.getGrade(), LocalDateTime.now().minusMinutes(11));
//        reservation2 = reservationRepository.save(reservation2);
//
//        //when
//        seatService.expireReservation();
//
//        //then
//        reservation1 = reservationRepository.findById(reservation1.getId());
//        reservation2 = reservationRepository.findById(reservation2.getId());
//        System.out.println("reservation1 = " + reservation1.getSeatId());
//        seat1 = seatRepository.findById(reservation1.getSeatId());
//        seat2 = seatRepository.findById(reservation2.getSeatId());
//        assertEquals(Reservation.State.EXPIRED, reservation1.getState());
//        assertEquals(Reservation.State.EXPIRED, reservation2.getState());
//        assertEquals(Seat.State.EMPTY, seat1.getState());
//        assertEquals(Seat.State.EMPTY, seat2.getState());
//
//
//    }
//
//
//
//}

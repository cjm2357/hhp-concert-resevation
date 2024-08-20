package com.example.concert_reservation.kafka;

import com.example.concert_reservation.domain.entity.Payment;
import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.entity.Seat;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.payment.PaymentRepository;
import com.example.concert_reservation.domain.payment.event.PaymentEvent;
import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import com.example.concert_reservation.domain.payment.message.PaymentMessageOutboxWriter;
import com.example.concert_reservation.domain.point.PointRepository;
import com.example.concert_reservation.domain.reservation.ReservationRepository;
import com.example.concert_reservation.domain.seat.SeatRepository;
import com.example.concert_reservation.domain.user.UserRepository;
import com.example.concert_reservation.fixture.PaymentFixture;
import com.example.concert_reservation.fixture.ReservationFixture;
import com.example.concert_reservation.fixture.SeatFixture;
import com.example.concert_reservation.fixture.UserFixture;
import com.example.concert_reservation.infra.payment.message.PaymentOutboxWriterImpl;
import com.example.concert_reservation.presentation.event.payment.PaymentEventListener;
import com.example.concert_reservation.presentation.scheduler.PaymentOutboxScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(partitions = 3, topics = { "payment" })
public class KafkaPaymentRepublishSchedulerTest {

    @Autowired
    PaymentMessageOutboxWriter paymentMessageOutboxWriter;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentEventListener paymentEventListener;
    @Autowired
    PaymentOutboxWriterImpl paymentOutboxWriter;
    @Autowired
    PaymentOutboxScheduler paymentOutboxScheduler;

    @BeforeEach
    void init() {
        User user = UserFixture.createUser(1, "유저1", 1, 10000l);
        pointRepository.save(user.getPoint());
        userRepository.save(user);
    }

    @Test
    void INIT_스케줄러_발행_테스트() throws Exception{

        //given
        Integer userId = 1;
        UUID tokenKey = UUID.randomUUID();
        User user = userRepository.findById(userId);
        Seat seat = SeatFixture.createSeat(1, 1, 1, 1,  Seat.State.RESERVED,8000l, "A");
        seatRepository.save(seat);

        Reservation reservation =
                ReservationFixture.creasteReservation(null, userId, 1, 1, 1, 1, Reservation.State.WAITING, 8000l, "A", LocalDateTime.now());
        reservation = reservationRepository.save(reservation);

        Payment payment = PaymentFixture.createPayment(null, userId, reservation.getId(), LocalDateTime.now());
        payment = paymentRepository.save(payment);


        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setPaymentId(payment.getId());
        paymentEvent.setReservationId(reservation.getId());
        paymentEvent.setPrice(reservation.getPrice());
        paymentEvent.setUserId(userId);
        paymentEvent.setTokenKey(tokenKey);
        //메세지생성후 DB에만 보관
        paymentEventListener.createOutBoxMessage(paymentEvent);

        //when
        paymentOutboxScheduler.republishPaymentMessage();
        Thread.sleep(3000);

        //then
        PaymentMessage paymentMessage= paymentOutboxWriter.findByPaymentId(payment.getId());

        //INIT, POINT, SEAT, FINISHED 순으로 발행됨
        //메세지가 발행되서 문제없이 진행됬다면 FINISHED에서 끝남
        assertEquals(PaymentMessage.PaymentState.PUBLISHED, paymentMessage.getState());
    }

}

package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Payment;
import com.example.concert_reservation.entity.Point;
import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.User;
import com.example.concert_reservation.service.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final TokenRepository tokenRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          ReservationRepository reservationRepository,
                          UserRepository userRepository,
                          PointRepository pointRepository,
                          TokenRepository tokenRepository)
    {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.pointRepository = pointRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public Payment pay(Payment payment) {
        Reservation reservation = reservationRepository.findById(payment.getReservationId());
        if (reservation == null) throw new NullPointerException("예약 정보가 없습니다.");
        if (reservation.getState() == Reservation.State.EXPIRED || reservation.getExpiredTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("결제 시간이 만료되었습니다.");
        }
        User user = new User();
        user.setId(payment.getUserId());
        user = userRepository.findById(user);

        if (user.getPoint() == null) throw new RuntimeException("유저의 포인트 정보가 없습니다.");
        if (user.getPoint().getAmount() < reservation.getPrice()) throw new RuntimeException("유저의 포인트가 결제금액보다 적습니다.");

        payment.setCreatedTime(LocalDateTime.now());
        payment = paymentRepository.save(payment);

        Point userPoint = user.getPoint();
        userPoint.setAmount(userPoint.getAmount() - reservation.getPrice());
        pointRepository.save(userPoint);

        reservation.setState(Reservation.State.COMPLETED);
        reservationRepository.save(reservation);

        tokenRepository.updateStateToExpiredByUserId(user.getId());



        return payment;
    }


}

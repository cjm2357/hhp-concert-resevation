package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.service.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation getReservation(Integer reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public Reservation changeReservationInfo(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation reserveSeat(Reservation reservation) {

        //COMPLETED or WAITING 항목 읽어오기
        List<Reservation> reservationHistory = reservationRepository.findReservedReservationBySeatId(reservation.getSeatId());

        if (reservationHistory.size() > 0) {
            throw new RuntimeException("이미 예약된 정보가 있습니다.");
        }

        // 성공후 좌석상태 변경
        reservation = reservationRepository.save(reservation);


        return reservation;

    }

    public List<Reservation> readExpiredReservation() {
       return reservationRepository.findExpiredReservation(LocalDateTime.now());
    }
}

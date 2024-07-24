package com.example.concert_reservation.domain.service;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.service.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation getReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            log.warn("no reservation information");
            throw new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND);
        }
        return reservation;
    }

    public Reservation changeReservationInfo(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation reserveSeat(Reservation reservation) {

        //COMPLETED or WAITING 항목 읽어오기
        List<Reservation> reservationHistory = reservationRepository.findReservedReservationBySeatId(reservation.getSeatId());

        if (reservationHistory.size() > 0) {
            throw new CustomException(CustomExceptionCode.RESERVATION_EXIST);
        }

        // 성공후 좌석상태 변경
        reservation = reservationRepository.save(reservation);


        return reservation;

    }

    public List<Reservation> readExpiredReservation() {
       return reservationRepository.findExpiredReservation(LocalDateTime.now());
    }
}

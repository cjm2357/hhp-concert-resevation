package com.example.concert_reservation.repository.reservation;

import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.service.reservation.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    //변경
    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationRepositoryImpl(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    public Reservation findById(Integer reservationId) {
        return reservationJpaRepository.findById(reservationId).orElse(null);
    }

    public Reservation findBySeatIdWithLock(Integer seatId) {
        return reservationJpaRepository.findBySeatIdWithLock(seatId);
    }

    public List<Reservation> findReservedReservationBySeatId(Integer seatId) {
        return reservationJpaRepository.findReservedReservationBySeatId(seatId);
    }
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    public List<Reservation> findExpiredReservation(LocalDateTime localDateTime) {
        return reservationJpaRepository.findExpiredReservation(localDateTime);
    }
}

package com.example.concert_reservation.domain.service.repository;

import com.example.concert_reservation.domain.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {

    Reservation findById(Integer reservationId);
    List<Reservation> findReservedReservationBySeatId(Integer seatId);
    Reservation findBySeatIdWithLock(Integer seatId);
    Reservation save(Reservation reservation);

    List<Reservation> findExpiredReservation(LocalDateTime localDateTime);


}

package com.example.concert_reservation.repository;


import com.example.concert_reservation.domain.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.seatId = :seatId")
    Reservation findBySeatIdWithLock(Integer seatId);
    Reservation save(Reservation reservation);

    @Query(value = "SELECT * FROM reservation r WHERE r.seat_id = :seatId and r.state <> 'EXPIRED'", nativeQuery = true)
    List<Reservation> findReservedReservationBySeatId(Integer seatId);

    @Query("SELECT r FROM Reservation r WHERE r.state = 'WAITING' AND r.expiredTime < :localDateTime")
    List<Reservation> findExpiredReservation(LocalDateTime localDateTime);

}

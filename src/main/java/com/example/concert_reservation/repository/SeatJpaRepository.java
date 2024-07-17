package com.example.concert_reservation.repository;

import com.example.concert_reservation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findByConcertIdAndState(Integer concertId, Seat.State state);

    List<Seat> findByScheduleIdAndState(Integer scheduleId, Seat.State state);

    @Modifying
    @Transactional
    @Query("UPDATE Seat s SET s.state = :state WHERE s.id IN :seatIdList")
    void saveAllStateBySeatId(List<Integer> seatIdList, Seat.State state);

    @Modifying
    @Transactional
    @Query("UPDATE Seat s SET s.state = :state WHERE s.id = :seatId")
    Seat saveSeatStateById(Integer seatId, Seat.State state);

}

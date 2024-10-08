package com.example.concert_reservation.infra.seat;

import com.example.concert_reservation.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat, Integer> {

    @Query("SELECT s FROM Seat s WHERE s.id = :seatId and s.state = 'EMPTY'")
    Optional<Seat> findAvailableSeat(Integer seatId);

    List<Seat> findByConcertIdAndState(Integer concertId, Seat.State state);

    List<Seat> findByScheduleIdAndState(Integer scheduleId, Seat.State state);

    @Modifying
    @Transactional
    @Query("UPDATE Seat s SET s.state = :state WHERE s.id IN :seatIdList")
    void saveAllStateBySeatId(List<Integer> seatIdList, Seat.State state);

}

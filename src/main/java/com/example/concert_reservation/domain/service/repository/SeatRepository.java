package com.example.concert_reservation.domain.service.repository;

import com.example.concert_reservation.domain.entity.Seat;

import java.util.List;

public interface SeatRepository {

    Seat findById(Integer seatId);
    Seat findByIdWithLock(Integer seatId);
    Seat save(Seat seat);
    List<Seat> findByConcertIdAndState(Integer concertId, Seat.State state);
    List<Seat> findByScheduleIdAndState(Integer concertId, Seat.State state);

    void saveAllStateBySeatId(List<Integer> seatIdList, Seat.State state);

    void saveSeatStateById(Integer seatId, Seat.State state);

}
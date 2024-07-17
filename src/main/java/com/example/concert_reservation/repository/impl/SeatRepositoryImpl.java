package com.example.concert_reservation.repository.impl;

import com.example.concert_reservation.domain.entity.Seat;
import com.example.concert_reservation.repository.SeatJpaRepository;
import com.example.concert_reservation.domain.service.repository.SeatRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    public SeatRepositoryImpl(SeatJpaRepository seatJpaRepository) {
        this.seatJpaRepository = seatJpaRepository;
    }

    public Seat findById(Integer seatId) {
        return seatJpaRepository.findById(seatId).get();
    }
    public List<Seat> findByConcertIdAndState(Integer concertId, Seat.State state) {
        return seatJpaRepository.findByConcertIdAndState(concertId, state);
    }

    public List<Seat> findByScheduleIdAndState(Integer scheduleId, Seat.State state){
        return seatJpaRepository.findByScheduleIdAndState(scheduleId, state);
    }

    public Seat save(Seat seat) {
        return seatJpaRepository.save(seat);
    }

    public void saveAllStateBySeatId(List<Integer> seatIdList, Seat.State state) {
        seatJpaRepository.saveAllStateBySeatId(seatIdList, state);
    }

    public Seat saveSeatStateById(Integer seatId, Seat.State state) {
        return seatJpaRepository.saveSeatStateById(seatId, state);
    }


}

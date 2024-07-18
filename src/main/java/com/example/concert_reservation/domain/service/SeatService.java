package com.example.concert_reservation.domain.service;

import com.example.concert_reservation.domain.service.repository.ReservationRepository;
import com.example.concert_reservation.domain.service.repository.SeatRepository;
import com.example.concert_reservation.domain.entity.Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    public SeatService(SeatRepository seatRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
    }

    public Seat getSeatById(Integer seatId) {
        return seatRepository.findById(seatId);
    }

    public List<Seat> getAvailableSeatList(Integer scheduleId) {
        return seatRepository.findByScheduleIdAndState(scheduleId, Seat.State.EMPTY);
    }

    public List<Seat> getAvailableSeatListByConcertId(Integer concertId) {
        return seatRepository.findByConcertIdAndState(concertId, Seat.State.EMPTY);
    }

    public void saveSeatState(Integer seatId, Seat.State state) {
         seatRepository.saveSeatStateById(seatId, state);
    }


    public void saveAllSeatState( List<Integer> seatIdList, Seat.State state) {
        seatRepository.saveAllStateBySeatId(seatIdList, state);
    }
}

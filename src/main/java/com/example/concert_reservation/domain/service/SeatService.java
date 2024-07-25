package com.example.concert_reservation.domain.service;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.service.repository.ReservationRepository;
import com.example.concert_reservation.domain.service.repository.SeatRepository;
import com.example.concert_reservation.domain.entity.Seat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SeatService {

    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    public SeatService(SeatRepository seatRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
    }

    public Seat getSeatById(Integer seatId) {
        Seat seat = seatRepository.findById(seatId);
        if (seat == null) {
            log.warn("not found seat of {}", seatId);
            throw new CustomException(CustomExceptionCode.SEAT_NOT_FOUND);
        }
        return seat;
    }

    public List<Seat> getAvailableSeatList(Integer scheduleId) {
        return seatRepository.findByScheduleIdAndState(scheduleId, Seat.State.EMPTY);
    }

    public List<Seat> getAvailableSeatListByConcertId(Integer concertId) {
        return seatRepository.findByConcertIdAndState(concertId, Seat.State.EMPTY);
    }

//    @Transactional
    public Seat updateSeatState(Integer seatId, Seat.State state) {
        Seat seat = seatRepository.findAvailableSeat(seatId);
        if (seat == null) {
            log.warn("not found seat of {}", seatId);
            throw new CustomException(CustomExceptionCode.SEAT_NOT_FOUND);
        }
        seat.setState(state);
        seat = seatRepository.save(seat);
        return seat;
    }

    public void saveAllSeatState( List<Integer> seatIdList, Seat.State state) {
        seatRepository.saveAllStateBySeatId(seatIdList, state);
    }
}

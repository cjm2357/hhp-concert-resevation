package com.example.concert_reservation.service;

import com.example.concert_reservation.entity.Reservation;
import com.example.concert_reservation.entity.Seat;
import com.example.concert_reservation.repository.SeatJpaRepository;
import com.example.concert_reservation.service.repository.ReservationRepository;
import com.example.concert_reservation.service.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Seat saveSeatState(Integer seatId, Seat.State state) {
        return seatRepository.saveSeatStateById(seatId, state);
    }

//    @Transactional
//    public Reservation reserveSeat(Reservation reservation) {
//
//        //COMPLETED or WAITING 항목 읽어오기
//        List<Reservation> reservationHistory = reservationRepository.findReservedReservationBySeatId(reservation.getSeatId());
//
//        if (reservationHistory.size() > 0) {
//            throw new RuntimeException("이미 예약된 정보가 있습니다.");
//        }
//
//        Seat seatInfo = seatRepository.findById(reservation.getSeatId());
//        reservation.setSeatGrade(seatInfo.getGrade());
//        reservation.setState(Reservation.State.WAITING);
//        reservation.setConcertId(seatInfo.getConcertId());
//        reservation.setPrice(seatInfo.getPrice());
//        reservation.setScheduleId(seatInfo.getScheduleId());
//        reservation.setSeatNo(seatInfo.getSeatNo());
//        reservation.setCreatedTime(LocalDateTime.now());
//        reservation.setExpiredTime(LocalDateTime.now().plusMinutes(Reservation.EXPIRE_TIME_FIVE_MIN));
//
//        // 성공후 좌석상태 변경
//        reservation = reservationRepository.save(reservation);
//
//        if (reservation.getId() != null) {
//            seatInfo.setState(Seat.State.RESERVED);
//            seatRepository.save(seatInfo);
//        }
//
//        return reservation;
//
//    }
//
//    @Transactional
//    public void expireReservation() {
//        //결제시간이 만료된 예약을 취소시킨다
//        List<Reservation> reservations = reservationRepository.findExpiredReservation(LocalDateTime.now());
//        List<Integer> seatIdList = new ArrayList<>();
//        reservations.stream().forEach(reservation -> {
//            seatIdList.add(reservation.getSeatId());
//            reservation.setState(Reservation.State.EXPIRED);
//        });
//
//        seatRepository.saveAllStateBySeatId(seatIdList, Seat.State.EMPTY);
//    }

    public void saveAllSeatState( List<Integer> seatIdList, Seat.State state) {
        seatRepository.saveAllStateBySeatId(seatIdList, Seat.State.EMPTY);
    }
}

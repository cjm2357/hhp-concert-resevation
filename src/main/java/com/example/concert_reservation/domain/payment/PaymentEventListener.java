package com.example.concert_reservation.domain.payment;

import com.example.concert_reservation.domain.common.DataPlatformEventPublisher;
import com.example.concert_reservation.domain.common.DataPlatformFailEvent;
import com.example.concert_reservation.domain.common.DataPlatformSendService;
import com.example.concert_reservation.domain.common.DataPlatformSuccessEvent;
import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.entity.Seat;
import com.example.concert_reservation.domain.point.PointService;
import com.example.concert_reservation.domain.reservation.ReservationService;
import com.example.concert_reservation.domain.seat.SeatService;
import com.example.concert_reservation.domain.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class PaymentEventListener {

    private final PointService pointService;
    private final SeatService seatService;
    private final ReservationService reservationService;
    private final TokenService tokenService;
    private final DataPlatformSendService dataPlatformSendService;

    private final DataPlatformEventPublisher dataPlatformEventPublisher;

    private final Set<Progress> progressSet = new HashSet<>();

    public enum Progress {
        POINT_PAY, SEAT_UPDATE, RESERVATION_UPDATE
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void paymentSuccessHandler(PaymentSuccessEvent event) {
        try {
            pointService.payPoint(event.getUser(), event.getReservation().getPrice());
            progressSet.add(Progress.POINT_PAY);

            seatService.updateSeatStateForPayment(event.getReservation().getSeatId(), Seat.State.RESERVED);
            progressSet.add(Progress.SEAT_UPDATE);

            event.getReservation().setState(Reservation.State.COMPLETED);
            reservationService.changeReservationInfo(event.getReservation());
            progressSet.add(Progress.RESERVATION_UPDATE);


            //dataPlatform은 외부 API이기 때문에 시간이 오래걸려 다른 event로 처리
            dataPlatformEventPublisher.success(new DataPlatformSuccessEvent(event.getReservation()));
        } catch (Exception e) {
            //진행단계에 따른 보상 트랜잭셕
            if (progressSet.contains(Progress.POINT_PAY)) pointService.savePoint(event.getUser(), event.getReservation().getPrice());
            if (progressSet.contains(Progress.SEAT_UPDATE)) seatService.updateSeatStateForPayment(event.getReservation().getSeatId(), Seat.State.RESERVED);
            if (progressSet.contains(Progress.RESERVATION_UPDATE)) {
                event.getReservation().setState(Reservation.State.WAITING);
                reservationService.changeReservationInfo(event.getReservation());
            }

            //dataPlatform은 외부 API이기 때문에 시간이 오래걸려 다른 event로 처리
            dataPlatformEventPublisher.fail(new DataPlatformFailEvent(event.getReservation()));
        }

//        dataPlatformSendService.sendMessage(Payment.SUCCESS_MSG, event.getReservation());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void paymentCompleteHandler(PaymentSuccessEvent event) {
        tokenService.expireToken(event.getTokenKey());
    }

}

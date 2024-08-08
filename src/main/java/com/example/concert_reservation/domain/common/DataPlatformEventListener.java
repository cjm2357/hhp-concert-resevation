package com.example.concert_reservation.domain.common;

import com.example.concert_reservation.domain.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class DataPlatformEventListener {

    private final DataPlatformSendService dataPlatformSendService;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void dataPlatformSuccessHandler(DataPlatformSuccessEvent event) {
        dataPlatformSendService.sendMessage(Payment.SUCCESS_MSG, event.getReservation());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void dataPlatformFailHandler(DataPlatformFailEvent event) {
        dataPlatformSendService.sendMessage(Payment.FAIL_MSG, event.getReservation());
    }
}

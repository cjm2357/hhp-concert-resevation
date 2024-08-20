package com.example.concert_reservation.presentation.scheduler;

import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import com.example.concert_reservation.domain.payment.message.PaymentMessageOutboxWriter;
import com.example.concert_reservation.domain.payment.message.PaymentMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentOutboxScheduler {

    private final PaymentMessageOutboxWriter paymentMessageOutboxWriter;
    private final PaymentMessageSender paymentMessageSender;

    //1분마다 작동
    @Scheduled(fixedRate = 1 * 60 * 1000)
    public void republishPaymentMessage() {

        log.info("start republish payment message scheduler");
        List<PaymentMessage> paymentMessages = paymentMessageOutboxWriter.findAllByState(PaymentMessage.PaymentState.INIT);
        for (PaymentMessage paymentMessage : paymentMessages) {
            if (paymentMessage.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(1l))) {
                paymentMessageSender.send(paymentMessage);
            }
        }

    }
}

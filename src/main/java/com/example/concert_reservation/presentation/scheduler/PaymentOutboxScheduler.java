package com.example.concert_reservation.presentation.scheduler;

import com.example.concert_reservation.application.concert.ConcertFacade;
import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import com.example.concert_reservation.domain.payment.message.PaymentMessageOutboxWriter;
import com.example.concert_reservation.domain.payment.message.PaymentMessageSender;
import com.example.concert_reservation.infra.payment.message.PaymentState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentOutboxScheduler {

    private final PaymentMessageOutboxWriter paymentMessageOutboxWriter;
    private final RedissonClient redissonClient;
    private final PaymentMessageSender paymentMessageSender;

    private final String REPUBLISH_PAYMENT_MESSAGE_KEY = "reservationScheduleLockKey";

    //10초마다 작동
    @Scheduled(cron = "*/10 * * * * *")
    public void republishPaymentMessage() {

        // 분산환경에서 스케줄 중복실행방지를 위해 분산 락 사용
        log.info("start republish payment message scheduler");
        RLock rLock = redissonClient.getLock(REPUBLISH_PAYMENT_MESSAGE_KEY);
        try {
            boolean available = rLock.tryLock(2, 5, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 이용불가");
            }
            List<PaymentMessage> paymentMessages = paymentMessageOutboxWriter.findAllByState(PaymentState.INIT);
            for (PaymentMessage paymentMessage : paymentMessages) {
                paymentMessageSender.send(paymentMessage);
            }

            log.info("success republish payment message scheduler");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Lock획득 실패");
        } finally {
            rLock.unlock();
        }
    }
}

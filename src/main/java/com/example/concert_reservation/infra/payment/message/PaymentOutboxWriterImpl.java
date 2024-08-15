package com.example.concert_reservation.infra.payment.message;

import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import com.example.concert_reservation.domain.payment.message.PaymentMessageOutboxWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentOutboxWriterImpl implements PaymentMessageOutboxWriter {

    private final PaymentJpaOutboxWriter paymentJpaOutboxWriter;

    @Override
    public PaymentMessage create(PaymentMessage paymentMessage) {
        PaymentOutboxEntity paymentOutboxEntity = PaymentOutboxEntity.builder()
                .paymentMessage(paymentMessage)
                .state(PaymentState.INIT)
                .build();

       return paymentJpaOutboxWriter.save(paymentOutboxEntity).toDomain();
    }

    @Override
    public PaymentMessage update(PaymentMessage paymentMessage) {
        PaymentOutboxEntity paymentOutboxEntity = PaymentOutboxEntity.builder()
                .paymentMessage(paymentMessage)
                .state(paymentMessage.getState())
                .build();

        return paymentJpaOutboxWriter.save(paymentOutboxEntity).toDomain();
    }

    public PaymentOutboxEntity findByPaymentId(Integer paymentId) {
        return paymentJpaOutboxWriter.findById(paymentId).get();
    }

    @Override
    public List<PaymentMessage> findAllByState(PaymentState state) {
        List<PaymentMessage> paymentMessages = new ArrayList<>();
        paymentJpaOutboxWriter.findAllByState(state).stream().forEach(entity->{
            paymentMessages.add(entity.toDomain());
        });
        return paymentMessages;
    }

}

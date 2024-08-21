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
        paymentMessage.setState(PaymentMessage.PaymentState.INIT);
       return paymentJpaOutboxWriter.save(paymentMessage);
    }

    @Override
    public PaymentMessage update(PaymentMessage paymentMessage) {
        return paymentJpaOutboxWriter.save(paymentMessage);
    }

    public PaymentMessage findByPaymentId(Integer paymentId) {
        return paymentJpaOutboxWriter.findById(paymentId).get();
    }

    @Override
    public List<PaymentMessage> findAllByState(PaymentMessage.PaymentState state) {
        return paymentJpaOutboxWriter.findAllByState(state);
    }

}

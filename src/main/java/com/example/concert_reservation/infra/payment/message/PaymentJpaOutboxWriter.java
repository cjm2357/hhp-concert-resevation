package com.example.concert_reservation.infra.payment.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PaymentJpaOutboxWriter extends JpaRepository<PaymentOutboxEntity, Integer> {

    public List<PaymentOutboxEntity> findAllByState(PaymentState state);

}

package com.example.concert_reservation.infra.payment;

import com.example.concert_reservation.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Integer> {

}

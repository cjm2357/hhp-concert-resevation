package com.example.concert_reservation.repository;

import com.example.concert_reservation.entity.Payment;
import com.example.concert_reservation.entity.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface PaymentJpaRepository extends JpaRepository<Payment, Integer> {

}

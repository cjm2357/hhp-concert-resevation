package com.example.concert_reservation.repository;

import com.example.concert_reservation.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert, Integer> {

}
package com.example.concert_reservation.infra.concert;

import com.example.concert_reservation.domain.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert, Integer> {

}

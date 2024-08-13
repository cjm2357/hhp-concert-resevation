package com.example.concert_reservation.infra.token;

import com.example.concert_reservation.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TokenJpaRepository extends JpaRepository<Token, Integer> {

    //만료된 마지막 토큰 찾기

    Token findByTokenKey(UUID key);

    List<Token> findByStateOrderById(Token.TokenState state);

    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.state = 'EXPIRED' WHERE t.expiredTime < :localDateTime and t.state = 'ACTIVATE'")
    void updateStateExpired(LocalDateTime localDateTime);

    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.state = 'EXPIRED' WHERE t.user.id = :userId ")
    void updateStateToExpiredByUserId(Integer userId);
}

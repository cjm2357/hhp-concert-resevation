package com.example.concert_reservation.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.plaf.nimbus.State;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {
    public enum TokenState {
        ACTIVATE, WAITING, EXPIRED
    }

    public static final int AVAILABLE_NUM = 50;
    public static final int EXPIRED_TIME_TEN_MIN = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User user;

    private UUID tokenKey;

    private LocalDateTime createdTime;
    private LocalDateTime expiredTime;

    @Enumerated(EnumType.STRING)
    private TokenState state;

    @Transient
    private Integer order;

    private Integer calcOrder(Token lastExpiredToken, Token lastNotExpiredToken) {
        int lastExpiredId = 0;
        int lastNotExpiredId = 0;
        if (lastExpiredToken != null) {
            lastExpiredId = lastExpiredToken.getId();
        }

        if (lastNotExpiredToken != null) {
            lastNotExpiredId = lastNotExpiredToken.getId();
        }

        int order = lastNotExpiredId - lastExpiredId;
        return order;
    }

    @Builder
    public Token(User user, UUID key, LocalDateTime createdTime, LocalDateTime expiredTime, TokenState state) {
        this.user = user;
        this.tokenKey = key;
        this.createdTime = createdTime;
        this.expiredTime = expiredTime;
        this.state = state;
    }

}



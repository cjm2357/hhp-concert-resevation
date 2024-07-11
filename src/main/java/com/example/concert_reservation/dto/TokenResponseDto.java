package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.Token;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponseDto {

    private Integer userId;
    private UUID key;
    private Integer order;

    public TokenResponseDto (Token token) {
        this.userId = token.getUser().getId();
        this.key = token.getTokenKey();
        this.order = token.getOrder();
    }

}

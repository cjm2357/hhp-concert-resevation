package com.example.concert_reservation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TokenResponseDto {

    private UUID key;
    private Integer order;
}

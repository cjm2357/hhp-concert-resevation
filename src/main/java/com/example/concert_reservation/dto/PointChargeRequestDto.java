package com.example.concert_reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointChargeRequestDto {

    private Integer userId;
    private Long amount;
}

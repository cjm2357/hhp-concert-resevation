package com.example.concert_reservation.dto;

import com.example.concert_reservation.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPointResponseDto {

    private Integer userId;
    private String userName;
    private Long amount;

    public UserPointResponseDto() {}
    public UserPointResponseDto(User user) {
        this.userId = user.getId();
        this.userName = user.getName();
        this.amount = user.getPoint().getAmount();
    }
    
}

package com.example.concert_reservation.dto;

import com.example.concert_reservation.domain.entity.User;
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
        if (user.getPoint() == null) {
            this.amount = 0l;
        } else {
            this.amount = user.getPoint().getAmount();
        }
    }
    
}

package com.example.concert_reservation.dto;

import com.example.concert_reservation.domain.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPointRequestDto {

    private Integer userId;
    private Long amount;

    public User toEntity() {
        User user = new User();
        user.setId(this.userId);
        return user;
    }
    
}

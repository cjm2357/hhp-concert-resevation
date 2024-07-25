package com.example.concert_reservation.domain.entity;

import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@Slf4j
@Table(name = "users")
public class User {

    @Id
    private Integer id;

    private String name;

    @OneToOne
    private Point point;

    public void isReservationUser(Integer reservationUserid) {
        if (this.id != reservationUserid) {
            log.warn("try payment different user");
            throw new CustomException(CustomExceptionCode.PAYMENT_DIFFERENT_USER);
        }
    }

    public void isPayable(long price) {
        if (point == null) {
            log.warn("{} user, no point information", id);
            throw new CustomException(CustomExceptionCode.USER_POINT_NOT_FOUND);
        }
        if (point.getAmount() < price || point.getAmount() < 0) {
            log.warn("{} user, points not enough");
            throw new CustomException(CustomExceptionCode.POINT_NOT_ENOUGH);
        }
    }
}

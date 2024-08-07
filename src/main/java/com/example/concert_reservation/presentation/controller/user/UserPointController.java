package com.example.concert_reservation.presentation.controller.user;

import com.example.concert_reservation.application.user.UserPointFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.dto.UserPointRequestDto;
import com.example.concert_reservation.dto.UserPointResponseDto;
import com.example.concert_reservation.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserPointController {

    private final UserPointFacade userPointFacade;

    public UserPointController(UserPointFacade userPointFacade) {
        this.userPointFacade = userPointFacade;
    }

    // 포인트 조회 API
    @PostMapping("/points")
    public ResponseEntity<?> readPoint(@RequestBody UserPointRequestDto dto) {
        if (dto.getUserId() != null) {
                User user = userPointFacade.getUserWithPoint(dto.getUserId());
            return ResponseEntity.ok(new UserPointResponseDto(user));
        } else {
            log.warn("no user id");
            throw new CustomException(CustomExceptionCode.USER_CAN_NOT_BE_NULL);
        }
    }

    // 포인트 충전 API
    @PostMapping("/points/charge")
    public ResponseEntity<?> chargePoint(@RequestBody UserPointRequestDto dto) {
        if (dto.getUserId() == null) {
            log.warn("no user id");
            throw new CustomException(CustomExceptionCode.USER_CAN_NOT_BE_NULL);
        }

        if (dto.getAmount() ==null || dto.getAmount() <= 0) {
            log.warn("request failed. charge point is under zero");
            throw new CustomException(CustomExceptionCode.CHARGE_POINT_NOT_BE_UNDER_ZERO);
        }

        User user = userPointFacade.chargePoint(dto.toEntity(), dto.getAmount());
        UserPointResponseDto responseDto = new UserPointResponseDto(user);
        return ResponseEntity.ok(responseDto);



    }
}

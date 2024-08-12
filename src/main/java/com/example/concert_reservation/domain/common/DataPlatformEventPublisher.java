package com.example.concert_reservation.domain.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataPlatformEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void success(DataPlatformSuccessEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void fail(DataPlatformFailEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}

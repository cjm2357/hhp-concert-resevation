package com.example.concert_reservation.service.unitTest;

import com.example.concert_reservation.domain.entity.Payment;
import com.example.concert_reservation.domain.payment.PaymentService;
import com.example.concert_reservation.domain.payment.PaymentRepository;
import com.example.concert_reservation.fixture.PaymentFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceUnitTest {

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentService paymentService;

    @Test
    void 결제_성공 () {
        //given
        Payment requestPayment = new Payment();
        requestPayment.setUserId(1);
        requestPayment.setReservationId(1);

        Payment expectedPayment = PaymentFixture.createPayment(1, 1, 1, LocalDateTime.now());

        when( paymentRepository.save(any())).thenReturn(expectedPayment);

        //when
        Payment payment = paymentService.pay(requestPayment);

        //then
        assertEquals(1, payment.getId());
        assertEquals(1, payment.getReservationId());
        assertEquals(1, payment.getUserId());
    }
}

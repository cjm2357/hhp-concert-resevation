package com.example.concert_reservation.presentation.consumer.payment;

import com.example.concert_reservation.application.concert.ConcertFacade;
import com.example.concert_reservation.application.token.TokenFacade;
import com.example.concert_reservation.application.user.UserPointFacade;
import com.example.concert_reservation.config.exception.CustomException;
import com.example.concert_reservation.config.exception.CustomExceptionCode;
import com.example.concert_reservation.domain.common.DataPlatform;
import com.example.concert_reservation.domain.entity.Concert;
import com.example.concert_reservation.domain.entity.Reservation;
import com.example.concert_reservation.domain.entity.User;
import com.example.concert_reservation.domain.payment.event.PaymentEvent;
import com.example.concert_reservation.domain.payment.message.PaymentMessage;
import com.example.concert_reservation.domain.payment.message.PaymentMessageOutboxWriter;
import com.example.concert_reservation.domain.payment.message.PaymentMessageSender;
import com.example.concert_reservation.infra.payment.message.PaymentState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentMessageConsumer {

    private final PaymentMessageOutboxWriter paymentMessageOutboxWriter;
    private final PaymentMessageSender paymentMessageSender;
    private final ConcertFacade concertFacade;
    private final UserPointFacade userPointFacade;
    private final TokenFacade tokenFacade;
    private final DataPlatform dataPlatform;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "${payment.topic}", groupId = "message-validation-check")
    @Transactional
    public void update(ConsumerRecord<String, String> record){
        try {
            PaymentMessage paymentMessage = objectMapper.readValue(record.value(), PaymentMessage.class) ;
            if (paymentMessage.getState() != PaymentState.INIT) {
                return;
            }
            paymentMessage.changeState(PaymentState.POINT);
            paymentMessageOutboxWriter.update(paymentMessage);
            paymentMessageSender.send(paymentMessage);
        } catch (JsonProcessingException e) {
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        }

    }

    @KafkaListener(topics = "${payment.topic}", groupId = "point-task")
    @Transactional
    public void runPointTask(ConsumerRecord<String, String> record) {
        PaymentMessage paymentMessage = null;
        PaymentEvent paymentEvent = null;
        try {
            paymentMessage = objectMapper.readValue(record.value(), PaymentMessage.class) ;
            if (paymentMessage.getState() != PaymentState.POINT) {
                return;
            }
            paymentEvent = objectMapper.readValue(paymentMessage.getMessage(), PaymentEvent.class);
            userPointFacade.usePoint(paymentEvent.getUserId(), paymentEvent.getPrice());
            paymentMessage.changeState(PaymentState.SEAT);
            paymentMessageOutboxWriter.update(paymentMessage);
            paymentMessageSender.send(paymentMessage);
        } catch (JsonProcessingException e) {
            log.error("runPointTask json parsing error");
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("failed point task of payment {}", paymentEvent.getPaymentId());
            concertFacade.deletePayment(paymentEvent.getPaymentId());
        }
    }

    @KafkaListener(topics = "${payment.topic}", groupId = "reservation-seat-task")
    @Transactional
    public void runReservationTask(ConsumerRecord<String, String> record) {
        PaymentMessage paymentMessage = null;
        PaymentEvent paymentEvent = null;
        try {
            paymentMessage = objectMapper.readValue(record.value(), PaymentMessage.class) ;
            if (paymentMessage.getState() != PaymentState.SEAT) {
                return;
            }
            paymentEvent = objectMapper.readValue(paymentMessage.getMessage(), PaymentEvent.class);
            concertFacade.updateSeatReservationState(paymentEvent.getReservationId());
            paymentMessage.changeState(PaymentState.FINISHED);
            paymentMessageOutboxWriter.update(paymentMessage);
            paymentMessageSender.send(paymentMessage);
        } catch (JsonProcessingException e) {
            log.error("runReservationTask json parsing error");
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        } catch (Exception e) {
            log.error("failed reservation task of reservation {}", paymentEvent.getPaymentId());
            concertFacade.deletePayment(paymentEvent.getPaymentId());
            User user = new User();
            user.setId(paymentEvent.getUserId());
            //결제한 금액만큼 충전해준다.
            userPointFacade.chargePoint(user, paymentEvent.getPrice());
        }
    }

    @KafkaListener(topics = "${payment.topic}", groupId = "token-task")
    public void runTokenTask(ConsumerRecord<String, String> record) {
        try {
            PaymentMessage paymentMessage = objectMapper.readValue(record.value(), PaymentMessage.class) ;
            if (paymentMessage.getState() != PaymentState.FINISHED) {
                return;
            }
            PaymentEvent paymentEvent = objectMapper.readValue(paymentMessage.getMessage(), PaymentEvent.class);
            // 실패해도 스케줄러에의해 자동완료
            tokenFacade.expireToken(paymentEvent.getTokenKey());

        } catch (JsonProcessingException e) {
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        }
    }

    @KafkaListener(topics = "${payment.topic}", groupId = "dataplatform-task")
    public void runDataPlatFormTask(ConsumerRecord<String, String> record) {
        try {
            PaymentMessage paymentMessage = objectMapper.readValue(record.value(), PaymentMessage.class) ;
            if (paymentMessage.getState() != PaymentState.FINISHED) {
                return;
            }
            PaymentEvent paymentEvent = objectMapper.readValue(paymentMessage.getMessage(), PaymentEvent.class);
            String text = "success";
            Reservation reservation = concertFacade.getReservation(paymentEvent.getReservationId());
            // 실패해도 롤백데이터는 X
            dataPlatform.sendMessage(text,reservation);

        } catch (JsonProcessingException e) {
            throw new CustomException(CustomExceptionCode.INVALID_JSON_TYPE);
        }
    }




}

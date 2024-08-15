package com.example.concert_reservation.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;



@Component
public class KafkaTestConsumer {


    public static String receivedMessage;
    private final Logger logger = LoggerFactory.getLogger(KafkaTestConsumer.class);

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void consume(ConsumerRecord<String, String> record) {

        logger.info("receivedMessage :: {}", record.value());
        receivedMessage = record.value();
    }

}

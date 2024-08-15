package com.example.concert_reservation.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "test-topic" })
public class kafkaMessageProduceConsumeTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTestConsumer kafkaTestConsumer;


    @Test
    void testKafkaSendMessage() throws InterruptedException {
        //given
        String message = "test-message";
        String topic = "test-topic";

        //when
        kafkaTemplate.send("test-topic", message);
        Thread.sleep(1000);

        //then
        assertEquals(message, kafkaTestConsumer.receivedMessage);

    }
}

package com.example.msa_db_sep_hongkd.ordering.service;

import com.example.msa_db_sep_hongkd.ordering.dto.RestockEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object event){
        kafkaTemplate.send(topic,event);
    }
}

package com.example.msa_db_sep_hongkd.common.config;

import com.example.msa_db_sep_hongkd.common.product.service.RestockEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.kafka-server}")
    private String kafkaServer;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offset;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offset);

        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        config.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "com.example.msa_db_sep_hongkd.*");
        config.put(JacksonJsonDeserializer.TYPE_MAPPINGS, // 기존의 restockEvent 하나만 topic으로 보내던 때완 다르게, Kafka의 topic이 늘었으니, 앞에 해당 Event명을 붙여서 topic 별 alias 등록
                            "orderCreated:com.example.msa_db_sep_hongkd.common.product.service.OrderCreatedEvent,"+
                            "orderCanceled:com.example.msa_db_sep_hongkd.common.product.service.OrderCanceledEvent,"+
                            "restock:com.example.msa_db_sep_hongkd.common.product.service.RestockEvent");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RestockEvent> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, RestockEvent> listenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory());
        return listenerContainerFactory;
    }
}
package com.ecommerce.Kafka;

import com.ecommerce.dto.OrderEvent;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    KafkaTemplate<String, OrderEvent> kafkaTemplate(){
        KafkaTemplate<String, OrderEvent> template = new KafkaTemplate<>(producerFactory());
        template.setObservationEnabled(true);
        return template;
    }

    @Bean
    ProducerFactory<String, OrderEvent> producerFactory(){
        Map<String, Object> config = kafkaProperties.buildProducerProperties();
        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );
        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(config);
    }
}

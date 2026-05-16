package com.ecommerce.Kafka;

import com.ecommerce.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderEvent(OrderEvent event){
        kafkaTemplate.send(
                "order-event", event.orderId().toString(),
                event).
                whenComplete((result, ex) -> {
                    if(ex != null){
                        log.error("Failed to send event {}", event.orderId(), ex);
                    }else{
                        var metadata = result.getRecordMetadata();
                        log.info("Event sent successfully: to topic {}, partition {}, offset {}",metadata.topic(), metadata.partition(), metadata.offset());
                    }
                });
        System.out.println("order event published "+ event);
    }
}

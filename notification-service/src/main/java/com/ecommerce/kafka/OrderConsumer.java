package com.ecommerce.kafka;

import com.ecommerce.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    @KafkaListener(topics = "order-event", groupId = "notification-service")
    public void consume(OrderEvent event){
        System.out.println("================================");

        System.out.println("ORDER EVENT RECEIVED");

        System.out.println(event);

        System.out.println("NOTIFICATION SENT");

        System.out.println("================================");
    }
}

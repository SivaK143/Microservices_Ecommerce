package com.ecommerce.dto;

import java.time.LocalDateTime;

public record OrderEvent(Long orderId,
                         String productName,
                         Integer quantity,
                         LocalDateTime createdAt) {
}

package com.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class OrderItemResponseDTO {

    private Long productId;
    private String productName;
    private int quantity;
    private Double price;
}

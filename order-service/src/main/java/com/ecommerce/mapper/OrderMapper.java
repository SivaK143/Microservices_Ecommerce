package com.ecommerce.mapper;

import com.ecommerce.dto.OrderItemResponseDTO;
import com.ecommerce.dto.OrderResponseDTO;
import com.ecommerce.entity.Order;

import java.util.List;

public class OrderMapper {

    public static OrderResponseDTO mapToDTO(Order order) {

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setUsername(order.getUsername());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        dto.setOrderDate(order.getOrderDate());

        List<OrderItemResponseDTO> itemDTOs = order.getItems()
                .stream()
                .map(item -> {
                    OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
                    itemDTO.setProductId(item.getProductId());
                    itemDTO.setProductName(item.getProductName());
                    itemDTO.setPrice(item.getPrice());
                    itemDTO.setQuantity(item.getQuantity());
                    return itemDTO;
                })
                .toList();

        dto.setItems(itemDTOs);

        return dto;
    }
}

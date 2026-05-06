package com.ecommerce.service;

import com.ecommerce.dto.OrderItemRequestDTO;
import com.ecommerce.dto.OrderRequestDTO;
import com.ecommerce.dto.OrderResponseDTO;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.mapper.OrderMapper;
import com.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestClient restClient;
    private final JwtService jwtService;

    public OrderService(RestClient restClient, JwtService jwtService, OrderRepository orderRepository) {
        this.restClient = restClient;
        this.orderRepository=orderRepository;
        this.jwtService=jwtService;
    }

    public void placeOrder(OrderRequestDTO request, String token) {

        String username = jwtService.extractUsername(token);

        Order order = new Order();
        order.setUsername(username);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequestDTO itemDTO : request.getItems()) {

            //  RestClient call
            ProductDTO product = restClient.get()
                    .uri("http://localhost:8082/products/{id}", itemDTO.getProductId())
                    .retrieve()
                    .body(ProductDTO.class);

            if (product == null) {
                throw new RuntimeException("Product not found");
            }

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setOrder(order);

            totalAmount += product.getPrice() * itemDTO.getQuantity();

            orderItems.add(item);
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);
    }

    //get Myorders
    public List<OrderResponseDTO> getMyOrders(String token) {

        String username = jwtService.extractUsername(token);

        List<Order> orders = orderRepository.findByUsername(username);

        return orders.stream()
                .map(OrderMapper::mapToDTO)
                .toList();
    }

    //get all orders
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::mapToDTO)
                .toList();
    }
}

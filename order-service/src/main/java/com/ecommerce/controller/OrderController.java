package com.ecommerce.controller;

import com.ecommerce.dto.OrderRequestDTO;
import com.ecommerce.dto.OrderResponseDTO;
import com.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final RestClient restClient;

    public OrderController(RestClient.Builder builder, OrderService orderService) {
        this.restClient = builder.build();
        this.orderService=orderService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(
            @RequestBody OrderRequestDTO request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        orderService.placeOrder(request, token);

        return ResponseEntity.ok("Order placed successfully");
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        return ResponseEntity.ok(orderService.getMyOrders(token));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/test")
    public String testLoadBalancing() {
        return restClient.get()
                .uri("http://product-service/products/instance")
                .retrieve()
                .body(String.class);
    }

    }

package com.ecommerce.controller;

import com.ecommerce.dto.OrderRequestDTO;
import com.ecommerce.dto.OrderResponseDTO;
import com.ecommerce.entity.ProductClient;
import com.ecommerce.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
//    private final RestClient restClient;
    private final ProductClient productClient;
    public OrderController(ProductClient productClient, OrderService orderService) {
        this.productClient = productClient;
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

    @CircuitBreaker(name = "testCircuitBreaker", fallbackMethod = "findPortFallback")
    @Retry(name = "testRetry")
    @GetMapping("/test/{number}")
    public String testLoadBalancing(@PathVariable int number) {
        log.info("==========In findPort Method ==========");
//        return restClient.get()
//                .uri("http://product-service/products/instance/{number}",number)
//                .retrieve()
//                .body(String.class);
        return productClient.getProducts(number);
    }

    public String findPortFallback(@PathVariable int number, Throwable exception){
        log.info("==========In findPortFallback ==========");
        log.error("product service failed for number due to {}",exception.getMessage());
        return "default port : 8080";
    }


    @Retry(name = "testRetryAttempts")
    @GetMapping("/test-product")
    public String callProductService() {
//        return restClient.get()
//                .uri("http://product-service/test/test")
//                .retrieve()
//                .body(String.class);
        return productClient.getMessage();
    }

    }

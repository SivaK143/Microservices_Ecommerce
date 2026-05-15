package com.ecommerce.entity;

import com.ecommerce.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/{productId}")
    ProductDTO getProductById(@PathVariable Long productId);

    @GetMapping("/products/instance/{number}")
    String getProducts(@PathVariable int number);

    @GetMapping("/test/test")
    String getMessage();
}

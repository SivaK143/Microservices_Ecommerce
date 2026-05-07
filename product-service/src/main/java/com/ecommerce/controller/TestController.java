package com.ecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RefreshScope
@RequestMapping("/test")
public class TestController {

    private final AtomicInteger counter= new AtomicInteger(0);


        @GetMapping("/test")
        public String getMessage() {
            int attempt = counter.incrementAndGet();
            if(attempt < 3){
                log.error("Simulated failure for counter less than 3 : counter {}", attempt);
                throw new RuntimeException(String.format("Simulated failure for counter less than 3 : counter :%d", attempt));
            }
            counter.set(0);
            return "Success after retries";
        }
    }

package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/test")
public class TestController {

        @Value("${my.test.message}")
        private String message;

        @GetMapping("/test")
        public String getMessage() {
            return message;
        }
    }

package com.ecommerce.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedrestClientBuilder(){
        return RestClient.builder();
    }

    @Bean
    public RestClient restClient(){
        return RestClient.create();
    }
//
//    @Bean
//    public RestClient.Builder loadBalancedrestClient(RestClient.Builder builder){
//        return RestClient.builder();
//    }
}

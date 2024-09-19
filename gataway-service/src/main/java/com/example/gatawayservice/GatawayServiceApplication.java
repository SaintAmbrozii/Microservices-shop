package com.example.gatawayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GatawayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatawayServiceApplication.class, args);
    }

}

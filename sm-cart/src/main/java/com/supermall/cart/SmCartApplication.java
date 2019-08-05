package com.supermall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SmCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmCartApplication.class,args);
    }
}

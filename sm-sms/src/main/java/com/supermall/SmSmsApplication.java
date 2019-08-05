package com.supermall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SmSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmSmsApplication.class,args);
    }
}

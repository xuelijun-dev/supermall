package com.supermall;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.supermall.item.mapper")
public class SmItemApplicaiton {
    public static void main(String[] args) {
        SpringApplication.run(SmItemApplicaiton.class,args);
    }
}

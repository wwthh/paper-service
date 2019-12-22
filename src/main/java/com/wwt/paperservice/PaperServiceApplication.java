package com.wwt.paperservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class PaperServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperServiceApplication.class, args);
    }

}

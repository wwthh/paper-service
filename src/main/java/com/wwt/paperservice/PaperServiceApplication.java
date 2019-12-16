package com.wwt.paperservice;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;


@SpringBootApplication
@EnableEurekaClient
public class PaperServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperServiceApplication.class, args);
    }

}

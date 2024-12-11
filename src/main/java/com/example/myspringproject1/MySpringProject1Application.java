package com.example.myspringproject1;

import com.example.myspringproject1.model.Client;
import com.example.myspringproject1.repos.ClientRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class MySpringProject1Application {

    private static final Logger logger = LoggerFactory.getLogger(MySpringProject1Application.class);

    public static void main(String[] args) {
        SpringApplication.run(MySpringProject1Application.class, args);
    }
    @Bean
    public CommandLineRunner demo(ClientRepo repository) {
        return (args) -> {


            logger.info("All clients:");
            repository.findAll().forEach(client -> logger.info(client.toString()));
        };
    }

}

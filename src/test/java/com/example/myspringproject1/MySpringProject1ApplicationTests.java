package com.example.lab2weblayer;

import com.example.myspringproject1.MySpringProject1Application;
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
public class MySpringProject1ApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(MySpringProject1Application.class);

    public static void main(String[] args) {
        SpringApplication.run(MySpringProject1Application.class, args);
    }
    @Bean
    public CommandLineRunner demo(ClientRepo repository) {
        return (args) -> {
//            // save a few customers
//            repository.save(new Client("a", "a","Testinis", "Testinis", "--", LocalDate.now()));
//            repository.save(new Client("a2", "a","Testinis", "Testinis", "--", LocalDate.now()));
//            repository.save(new Client("a3", "a","Testinis", "Testinis", "--", LocalDate.now()));
//            repository.save(new Client("a4", "a","Testinis", "Testinis", "--", LocalDate.now()));
//
//
//            logger.info("All clients found with findAll():");
//            logger.info("-------------------------------");
//            for (Client client : repository.findAll()) {
//                logger.info(client.toString());
//            }
//            logger.info("");
//
//            // fetch person by id
//            Optional<Client> person = repository.findById(1);
//            logger.info("User found with findById(1L):");
//            logger.info("--------------------------------");
//            logger.info(person.toString());
//            logger.info("");
//            logger.info("");
        };
    }
}

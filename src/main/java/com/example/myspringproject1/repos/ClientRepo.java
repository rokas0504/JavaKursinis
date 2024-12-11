package com.example.myspringproject1.repos;

import com.example.myspringproject1.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client, Integer> {
        boolean existsByLogin(String login);

    Client getClientByLoginAndPassword(String login, String password);

}

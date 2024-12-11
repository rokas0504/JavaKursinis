package com.example.myspringproject1.repos;

import com.example.myspringproject1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u")
    List<User> findAllUsers();

    List<User> findByLogin(String login);

    User findByLoginAndPassword(String login, String password);
}

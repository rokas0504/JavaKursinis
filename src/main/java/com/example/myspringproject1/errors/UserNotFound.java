package com.example.myspringproject1.errors;

public class UserNotFound extends RuntimeException {

    public UserNotFound(Integer id) {
        super("Could not find user " + id);
    }
}

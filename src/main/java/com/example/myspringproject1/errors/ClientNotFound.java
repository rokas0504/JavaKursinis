package com.example.myspringproject1.errors;

public class ClientNotFound extends RuntimeException {

    public ClientNotFound(Integer id) {
        super("Could not find client " + id);
    }
}

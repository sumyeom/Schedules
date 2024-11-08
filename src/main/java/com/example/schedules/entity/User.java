package com.example.schedules.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class User {
    private String uid;
    private String name;
    private String password;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public User(String name, String password, String email){
        this.uid = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.email = email;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }


}

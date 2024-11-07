package com.example.schedules.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long scheduleId;
    private String userName;
    private String password;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Schedule(String userName, String password, String title, String content){
        this.userName = userName;
        this.password = password;
        this.title = title;
        this.content = content;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public Schedule(Long scheduleId, String userName, String title, String content, LocalDateTime createdDate, LocalDateTime updatedDate){
        this.scheduleId = scheduleId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public void update(String title, String content, String userName){
        this.title = title;
        this.content = content;
        this.userName = userName;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void update(String userName){
    }

}

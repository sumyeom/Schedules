package com.example.schedules.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long scheduleId;
    private String uid;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Schedule(String uid, String title, String content){
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public Schedule(String title, String content){
        this.title = title;
        this.content = content;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

}

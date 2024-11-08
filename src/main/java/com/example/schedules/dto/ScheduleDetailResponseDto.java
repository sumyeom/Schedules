package com.example.schedules.dto;

import com.example.schedules.entity.Schedule;
import com.example.schedules.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleDetailResponseDto {
    private Long scheduleId;
    private String name;
    private String email;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public ScheduleDetailResponseDto(Schedule schedule, User user){
        this.scheduleId = schedule.getScheduleId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.createdDate = schedule.getCreatedDate();
        this.updatedDate = schedule.getUpdatedDate();
    }
}

package com.example.schedules.dto;

import com.example.schedules.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleDetailResponseDto {
    private Long scheduleId;
    private String userName;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public ScheduleDetailResponseDto(Schedule schedule){
        this.scheduleId = schedule.getScheduleId();
        this.userName = schedule.getUserName();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.createdDate = schedule.getCreatedDate();
        this.updatedDate = schedule.getUpdatedDate();
    }
}

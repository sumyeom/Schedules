package com.example.schedules.dto;

import com.example.schedules.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long scheduleId;

    public ScheduleResponseDto(Schedule schedule){
        this.scheduleId = schedule.getScheduleId();
    }
}

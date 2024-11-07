package com.example.schedules.service;

import com.example.schedules.dto.ScheduleDetailResponseDto;
import com.example.schedules.dto.ScheduleRequestDto;
import com.example.schedules.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto);
    List<ScheduleDetailResponseDto> findAllSchedules(String updatedDate, String userName);
    ScheduleDetailResponseDto findScheduleById(Long id);
    ScheduleResponseDto updateSchedule(Long id, String userName, String password, String title, String content);
    void deleteSchedule(Long id);
}

package com.example.schedules.repository;

import com.example.schedules.dto.ScheduleDetailResponseDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    ScheduleResponseDto savedSchedule(Schedule schedule);
    List<ScheduleDetailResponseDto> findAllSchedules(String updatedDate, String userName);
    Optional<Schedule> findScheduleById(Long id);
    Schedule findScheduleByIdOrElseThrow(Long id);
    int updateSchdule(Long id, String title, String content, String userName);
    int deleteSchedule(Long id);
}

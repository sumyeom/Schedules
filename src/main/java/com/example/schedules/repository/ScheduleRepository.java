package com.example.schedules.repository;

import com.example.schedules.dto.ScheduleDetailResponseDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;
import com.example.schedules.entity.User;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    ScheduleResponseDto savedSchedule(Schedule schedule, User user);
    ScheduleResponseDto saveScheduleWithUser(Schedule schedule, User user);
    List<ScheduleDetailResponseDto> findAllSchedules(String updatedDate, String userName);
    Optional<Schedule> findScheduleById(Long id);
    Schedule findScheduleByIdOrElseThrow(Long id);
    User findUserByUidOrElseThrow(String uid);
    int updateSchdule(Long id, String title, String content, String uid, String userName);
    int deleteSchedule(Long id);
    List<ScheduleDetailResponseDto> getSchedules(int page, int size);
}

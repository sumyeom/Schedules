//package com.example.schedules.repository;
//
//import com.example.schedules.dto.ScheduleDetailResponseDto;
//import com.example.schedules.entity.Schedule;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//
//@Repository
//public class ScheduleRepositoryImpl implements ScheduleRepository {
//
//    private final Map<Long, Schedule> scheduleList = new HashMap<>();
//
//    @Override
//    public Schedule savedSchedule(Schedule schedule) {
//
//        //schedule 식별자 자동 생성
//        Long scheduleId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;
//        schedule.setScheduleId(scheduleId);
//        scheduleList.put(scheduleId, schedule);
//        return schedule;
//    }
//
//    @Override
//    public List<ScheduleDetailResponseDto> findAllSchedules() {
//
//        List<ScheduleDetailResponseDto> allSchdules = new ArrayList<>();
//
//        for(Schedule schedule : scheduleList.values()){
//            ScheduleDetailResponseDto responseDto = new ScheduleDetailResponseDto(schedule);
//            allSchdules.add(responseDto);
//        }
//        return allSchdules;
//    }
//
//    @Override
//    public Schedule findScheduleById(Long id) {
//        return scheduleList.get(id);
//    }
//}

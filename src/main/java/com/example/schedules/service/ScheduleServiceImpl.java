package com.example.schedules.service;

import com.example.schedules.dto.ScheduleDetailResponseDto;
import com.example.schedules.dto.ScheduleRequestDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;
import com.example.schedules.entity.User;
import com.example.schedules.exception.CustomException;
import com.example.schedules.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.schedules.exception.ErrorCode.INVALID_PASSWORD;
import static com.example.schedules.exception.ErrorCode.SCHEDULE_NOT_FOUND;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        //요청 받은 데이터로 Schedule 객체 생성 ID 없음
        Schedule schedule = new Schedule(requestDto.getTitle(), requestDto.getContent());
        User user = new User(requestDto.getUserName(), requestDto.getPassword(), requestDto.getEmail());
        return scheduleRepository.saveScheduleWithUser(schedule, user);
    }

    @Transactional
    @Override
    public List<ScheduleDetailResponseDto> findAllSchedules(String updatedDate, String userName) {
        List<ScheduleDetailResponseDto> allSchedules = scheduleRepository.findAllSchedules(updatedDate, userName);
        return allSchedules;
    }

    @Transactional
    @Override
    public ScheduleDetailResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        if(schedule!=null){
            User user = scheduleRepository.findUserByUidOrElseThrow(schedule.getUid());
            return new ScheduleDetailResponseDto(schedule,user);
        }else {
            throw new CustomException(SCHEDULE_NOT_FOUND);
        }

    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String userName, String password, String title, String content) {
        if(userName == null || password == null || title == null || content == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
        }
        Optional<Schedule> schedule = scheduleRepository.findScheduleById(id);
        if(!schedule.isEmpty()){
            User user = scheduleRepository.findUserByUidOrElseThrow(schedule.get().getUid());

            if(user.getPassword().equals(password)){
                int updateRow = scheduleRepository.updateSchdule(id, title, content, user.getUid(), userName);
                if(updateRow == 0){
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
                }
            }else{
                //throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid password");
                throw new CustomException(INVALID_PASSWORD);
            }
        }else{
            throw new CustomException(SCHEDULE_NOT_FOUND);
        }

        return new ScheduleResponseDto(schedule.get());
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        Optional<Schedule> schedule = scheduleRepository.findScheduleById(id);
        if(!schedule.isEmpty()){
            User user = scheduleRepository.findUserByUidOrElseThrow(schedule.get().getUid());
            if(user.getPassword().equals(password)){
                int deletedRow = scheduleRepository.deleteSchedule(id);
                if(deletedRow == 0){
                    //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
                    throw new CustomException(SCHEDULE_NOT_FOUND);
                }
            }else{
                throw new CustomException(INVALID_PASSWORD);
            }
        }else{
            throw new CustomException(SCHEDULE_NOT_FOUND);
        }

    }

    @Override
    public List<ScheduleDetailResponseDto> getSchedules(int page, int size) {
        if(page <= 0 || size <= 0){
            return Collections.emptyList();
        }
        return scheduleRepository.getSchedules(page, size);
    }
}

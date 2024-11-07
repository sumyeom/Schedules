package com.example.schedules.service;

import com.example.schedules.dto.ScheduleDetailResponseDto;
import com.example.schedules.dto.ScheduleRequestDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;
import com.example.schedules.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto requestDto) {
        //요청 받은 데이터로 Schedule 객체 생성 ID 없음
        Schedule schedule = new Schedule(requestDto.getUserName(),requestDto.getPassword(),requestDto.getTitle(), requestDto.getContent());

        return scheduleRepository.savedSchedule(schedule);
    }

    @Override
    public List<ScheduleDetailResponseDto> findAllSchedules(String updatedDate, String userName) {
        List<ScheduleDetailResponseDto> allSchedules = scheduleRepository.findAllSchedules(updatedDate, userName);
        return allSchedules;
    }

    @Override
    public ScheduleDetailResponseDto findScheduleById(Long id) {

//        Optional<Schedule> optionalSchedule = scheduleRepository.findScheduleById(id);
//
//        if(optionalSchedule.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist it = " + id);
//        }
//
//        return new ScheduleDetailResponseDto(optionalSchedule.get());
        Schedule schedule = scheduleRepository.findScheduleByIdOrElseThrow(id);
        return new ScheduleDetailResponseDto(schedule);
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String userName, String password, String title, String content) {
        if(userName == null || password == null || title == null || content == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
        }
        Optional<Schedule> schedule = scheduleRepository.findScheduleById(id);
        if(schedule.get().getPassword().equals(password)){
            int updateRow = scheduleRepository.updateSchdule(id, title, content, userName);
            if(updateRow == 0){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data has been modified.");
            }
        }else{
            //  비밀번호 예외처리
        }

        return new ScheduleResponseDto(schedule.get());
    }

    @Override
    public void deleteSchedule(Long id) {
        int deletedRow = scheduleRepository.deleteSchedule(id);
        if(deletedRow == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }
}

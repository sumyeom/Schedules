package com.example.schedules.controller;

import com.example.schedules.dto.ScheduleDetailResponseDto;
import com.example.schedules.dto.ScheduleRequestDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/home/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 일정 생성 API
     * @param {@link ScheduleRequestDto} 일정 생성 요청 객체
     * @return {@link ResponseEntity<ScheduleResponseDto>} JSON 응답
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto){

        // Service Layer 호출 및 응답
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    /**
     * 일정 단건 조회 API
     * @param schedulesId 식별자
     * @return {@link ResponseEntity<ScheduleDetailResponseDto>} JSON 응답
     */
    @GetMapping("/{schedulesId}")
    public ResponseEntity<ScheduleDetailResponseDto> findScheduleById(@PathVariable Long schedulesId){

        return new ResponseEntity<>(scheduleService.findScheduleById(schedulesId),HttpStatus.OK);
    }

    /**
     * 일정 전체 조회 API
     * @return {@link List<ScheduleDetailResponseDto>} JSON 응답
     */
    @GetMapping
    public List<ScheduleDetailResponseDto> findAllSchedules(
            @RequestParam(required = false) String updatedDate,
            @RequestParam(required = false) String userName){

        return scheduleService.findAllSchedules(updatedDate, userName);
    }

    /**
     * 일정 전체 수정 API
     * @param schedulesId 식별자
     * @param {@link requestDto} 일정 수정 요청 객체
     * @return {@link ResponseEntity<ScheduleResponseDto>} JSON 응답
     * @exception ResponseStatusException 요청 필수값이 없는 경우 400 BAD Request, 식별자로 조회된 Memo가 없는 경우 404 Not Found
     */
    @PutMapping("/{schedulesId}")
    public ResponseEntity<ScheduleResponseDto> updateMemo(
            @PathVariable Long schedulesId,
            @RequestBody ScheduleRequestDto requestDto
    ){
        return new ResponseEntity<>(scheduleService.updateSchedule(schedulesId, requestDto.getUserName(),requestDto.getPassword(),requestDto.getTitle(), requestDto.getContent()),HttpStatus.OK);
    }

    /**
     * 메모 삭제 API
     * @param schedulesId 식별자
     * @return {@link ResponseEntity<Void>} 성공시 Data 없이 200OK 상태코드만 응답.
     * @exception ResponseStatusException 식별자로 조회된 Memo가 없는 경우 404 Not Found
     */
    @DeleteMapping("/{schedulesId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long schedulesId){
        scheduleService.deleteSchedule(schedulesId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


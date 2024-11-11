package com.example.schedules.controller;

import com.example.schedules.dto.*;
import com.example.schedules.exception.CustomException;
import com.example.schedules.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.Map;

import static com.example.schedules.exception.ErrorCode.INVALID_INPUT_PARAM;
import static com.example.schedules.exception.ErrorCode.INVALID_INPUT_VALUE;

@RestController
@RequestMapping("/home/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final View error;

    public ScheduleController(ScheduleService scheduleService, View error) {
        this.scheduleService = scheduleService;
        this.error = error;
    }

    /**
     * 일정 생성 API
     * @param {@link ScheduleRequestDto} 일정 생성 요청 객체
     * @return {@link ResponseEntity<ScheduleResponseDto>} JSON 응답
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody @Valid ScheduleRequestDto dto, BindingResult bindingResult){
        // 유효성 검사
        if(bindingResult.hasErrors()) {
            bindErrorMessage(bindingResult);
        }
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
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam Map<String, String> allParams)
    {
        boolean invalidParams = allParams.keySet().stream()
                .anyMatch(key -> !key.equals("updatedDate") && !key.equals("name") && !key.equals("page") && !key.equals("size"));

        if (invalidParams) {
            throw new CustomException(INVALID_INPUT_PARAM);
        }
        return scheduleService.findAllSchedules(updatedDate, name,page,size);
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
            @Valid @RequestBody ScheduleUpdateRequestDto requestDto,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) {
            bindErrorMessage(bindingResult);
        }
        return new ResponseEntity<>(scheduleService.updateSchedule(schedulesId, requestDto.getUserName(),requestDto.getPassword(),requestDto.getTitle(), requestDto.getContent()),HttpStatus.OK);
    }

    /**
     * 메모 삭제 API
     * @param schedulesId 식별자
     * @return {@link ResponseEntity<Void>} 성공시 Data 없이 200OK 상태코드만 응답.
     * @exception ResponseStatusException 식별자로 조회된 Memo가 없는 경우 404 Not Found
     */
    @DeleteMapping("/{schedulesId}")
    public ResponseEntity<Void> deleteMemo(
            @PathVariable Long schedulesId,
            @Valid @RequestBody ScheduleDeleteRequestDto requestDto,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()) {
            bindErrorMessage(bindingResult);
        }
        scheduleService.deleteSchedule(schedulesId, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    /**
//     * 메모 페이징 API
//     * @param page 페이지 번호
//     * @param size 페이지 사이즈
//     * @return {@link ResponseEntity<List<ScheduleDetailResponseDto>>} JSON 응답
//     */
//    @GetMapping("/pages")
//    public ResponseEntity<List<ScheduleDetailResponseDto>> getSchedulesPage(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size
//    ){
//        List<ScheduleDetailResponseDto> schedulePage = scheduleService.getSchedules(page,size);
//        return new ResponseEntity<>(schedulePage, HttpStatus.OK);
//    }

    /**
     * Error Message 바인딩 후 throw하는 메서드
     * @param bindingResult
     */
    private void bindErrorMessage(BindingResult bindingResult){
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessage.append("[")
                    .append(error.getField())
                    .append("] ")
                    .append(":")
                    .append(error.getDefaultMessage());

        }
        throw new CustomException(INVALID_INPUT_VALUE,errorMessage.toString());
    }

}


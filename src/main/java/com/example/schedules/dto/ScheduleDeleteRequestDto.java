package com.example.schedules.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ScheduleDeleteRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}

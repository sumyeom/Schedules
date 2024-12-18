package com.example.schedules.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private String scheduleId;
    @NotBlank(message = "이름을 입력해주세요.")
    private String userName;
    @Email(message="이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @Size(min = 0,max=200,message="일정 내용은 200자 이내로 입력해주세요")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}

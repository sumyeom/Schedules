package com.example.schedules.repository;

import com.example.schedules.dto.ScheduleDetailResponseDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto savedSchedule(Schedule schedule) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_name", schedule.getUserName());
        parameters.put("password", schedule.getPassword());
        parameters.put("title", schedule.getTitle());
        parameters.put("content", schedule.getContent());

        parameters.put("created_date", schedule.getCreatedDate() != null ? schedule.getCreatedDate() : LocalDateTime.now());
        parameters.put("updated_date", schedule.getUpdatedDate() != null ? schedule.getUpdatedDate() : LocalDateTime.now());

        // 저장 후 생성된 key값을 Number 타입으로 반환하는 메서드
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new ScheduleResponseDto(key.longValue());
    }

    @Override
    public List<ScheduleDetailResponseDto> findAllSchedules(String updatedDate, String userName) {
        String sql = "SELECT * FROM schedule WHERE (DATE(updated_date) = ? OR ? IS NULL) AND (user_name = ? OR ? IS NULL) ORDER BY updated_date DESC";

        // Optional 조건에 맞는 파라미터 설정
        return jdbcTemplate.query(
                sql,
                scheduleRowMapper(),
                updatedDate, updatedDate, userName, userName
        );
    }

    @Override
    public Optional<Schedule> findScheduleById(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", scheduleRowMapperV2(), id);

        return result.stream().findAny();
    }

    @Override
    public Schedule findScheduleByIdOrElseThrow(Long id) {
        List<Schedule> result = jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", scheduleRowMapperV2(), id);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    @Override
    public int updateSchdule(Long id, String title, String content, String userName) {
        return jdbcTemplate.update("UPDATE schedule SET title = ? , content = ?, user_name = ? WHERE id = ?",title, content,userName,id);
    }

    @Override
    public int deleteSchedule(Long id) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE id = ?",id);
    }

    private RowMapper scheduleRowMapper(){
        return new RowMapper<ScheduleDetailResponseDto>(){

            @Override
            public ScheduleDetailResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleDetailResponseDto(
                        rs.getLong("id"),
                        rs.getString("user_name"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("created_date").toLocalDateTime(),
                        rs.getTimestamp("updated_date").toLocalDateTime()
                );
            }
        };
    }

    private RowMapper<Schedule> scheduleRowMapperV2(){
        return new RowMapper<Schedule>(){

            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getObject("created_date", LocalDateTime.class),
                        rs.getObject("updated_date", LocalDateTime.class)
                );
            }
        };
    }
}

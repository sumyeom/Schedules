package com.example.schedules.repository;

import com.example.schedules.dto.ScheduleDetailResponseDto;
import com.example.schedules.dto.ScheduleResponseDto;
import com.example.schedules.entity.Schedule;
import com.example.schedules.entity.User;
import com.example.schedules.exception.CustomException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.schedules.exception.ErrorCode.SCHEDULE_NOT_FOUND;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ScheduleResponseDto savedSchedule(Schedule schedule, User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", schedule.getTitle());
        parameters.put("content", schedule.getContent());

        parameters.put("created_date", schedule.getCreatedDate() != null ? schedule.getCreatedDate() : LocalDateTime.now());
        parameters.put("updated_date", schedule.getUpdatedDate() != null ? schedule.getUpdatedDate() : LocalDateTime.now());

        // 저장 후 생성된 key값을 Number 타입으로 반환하는 메서드
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new ScheduleResponseDto(key.longValue());
    }

    @Override
    public ScheduleResponseDto saveScheduleWithUser(Schedule schedule, User user) {
        // 이메일과 이름을 사용하여 user 테이블에서 작성자 정보를 조회
        String selectUserQuery = "SELECT * FROM user WHERE email = ? AND name = ?";
        User selectUser = jdbcTemplate.query(selectUserQuery, new Object[]{user.getEmail(), user.getName()}, rs -> {
            if (rs.next()) {
                return new User(
                        rs.getString("uid"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getTimestamp("created_date").toLocalDateTime(),
                        rs.getTimestamp("updated_date").toLocalDateTime()
                );
            }
            return null;
        });

        String userUid;
        if (selectUser == null) {
            // 작성자가 없는 경우, user 테이블에 새로운 작성자 삽입
            //userUid = UUID.randomUUID().toString();
            User newUser = new User(user.getName(), user.getPassword(),user.getEmail());
            userUid = newUser.getUid();
            String insertUserQuery = "INSERT INTO user (uid, name, password, email) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertUserQuery, newUser.getUid(), newUser.getName(), newUser.getPassword(), newUser.getEmail());
        } else {
            // 작성자가 이미 존재하는 경우, uid 가져오기
            userUid = selectUser.getUid();
        }

        // 해당 uid와 함께 schedule 테이블에 삽입
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("uid",userUid);
        parameters.put("title", schedule.getTitle());
        parameters.put("content", schedule.getContent());

        parameters.put("created_date", schedule.getCreatedDate() != null ? schedule.getCreatedDate() : LocalDateTime.now());
        parameters.put("updated_date", schedule.getUpdatedDate() != null ? schedule.getUpdatedDate() : LocalDateTime.now());

        // 저장 후 생성된 key값을 Number 타입으로 반환하는 메서드
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new ScheduleResponseDto(key.longValue());
    }

    @Override
    public List<ScheduleDetailResponseDto> findAllSchedules(String updatedDate, String name) {
        // schedule 테이블과 user테이블을 join하여 조회(schedule - updatedDate / user - name)
        String sql = "SELECT s.id, u.name, u.email, s.title, s.content, s.created_date, s.updated_date" +
                " FROM schedule s" +
                " JOIN user u ON s.uid = u.uid" +
                " AND (DATE(s.updated_date) = ? OR ? IS NULL)" +
                " AND (u.name = ? OR ? IS NULL)" +
                " ORDER BY s.updated_date DESC";

        return jdbcTemplate.query(
                sql,
                scheduleRowMapper(),
                updatedDate, updatedDate, name, name
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

        return result.stream().findAny().orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND));
    }

    @Override
    public User findUserByUidOrElseThrow(String uid) {
        List<User> result = jdbcTemplate.query("SELECT * FROM user WHERE uid = ?", userRowMapper(), uid);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist uid = " + uid));
    }

    @Override
    public int updateSchdule(Long id, String title, String content, String uid, String userName) {
        int updateRow = 0;
        updateRow = jdbcTemplate.update("UPDATE schedule SET title = ? , content = ? WHERE id = ?",title, content, id);
        updateRow += jdbcTemplate.update("UPDATE user SET name = ? WHERE uid = ?", userName, uid);
        return updateRow;
    }

    @Override
    public int deleteSchedule(Long id) {
        return jdbcTemplate.update("DELETE FROM schedule WHERE id = ?",id);
    }

    @Override
    public List<ScheduleDetailResponseDto> getSchedules(int page, int size) {
        int offset = (page - 1) * size;
        String sql = "SELECT s.id, u.name, u.email, s.title, s.content, s.created_date, s.updated_date" +
                " FROM schedule s" +
                " JOIN user u ON s.uid = u.uid" +
                " ORDER BY s.updated_date DESC" +
                " LIMIT ? OFFSET ?";

        return jdbcTemplate.query(
                sql,
                scheduleRowMapper(),
                size, offset
        );
    }

    private RowMapper<ScheduleDetailResponseDto> scheduleRowMapper(){
        return new RowMapper<ScheduleDetailResponseDto>(){

            @Override
            public ScheduleDetailResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleDetailResponseDto(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
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
                        rs.getString("uid"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getObject("created_date", LocalDateTime.class),
                        rs.getObject("updated_date", LocalDateTime.class)
                );
            }
        };
    }
    private RowMapper<User> userRowMapper(){
        return new RowMapper<User>(){

            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(
                        rs.getString("uid"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getObject("created_date", LocalDateTime.class),
                        rs.getObject("updated_date", LocalDateTime.class)
                );
            }
        };
    }
}

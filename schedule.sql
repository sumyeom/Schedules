-- Create Table
    -- schedule table
    CREATE TABLE schedule (
          id INT PRIMARY KEY AUTO_INCREMENT,
          user_id CHAR(36) NOT NULL,
          title VARCHAR(50) NOT NULL,
          content VARCHAR(100) NOT NULL,
          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
          updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
          group_name VARCHAR(30),
          FOREIGN KEY (user_id) REFERENCES user (uid)
    );
    -- user table
    CREATE TABLE user(
         uid CHAR(36) NOT NULL PRIMARY KEY ,
         id VARCHAR(30) NOT NULL ,
         name VARCHAR(30) NOT NULL,
         password VARCHAR(100) NOT NULL,
         email VARCHAR(50) NOT NULL,
         created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
         updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP
    );

-- Insert Value
    -- Insert in schedule table
    INSERT INTO schedule (user_id, title, content)
    VALUES ('9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3','11월 1일 데일리 스크럼', '1. Spring 강의 듣기 | 2. Lv.0 과제 완료' );
    -- Insert in user table
    INSERT INTO schedule (user_id, title, content)
    VALUES ('9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3','11월 1일 데일리 스크럼', '1. Spring 강의 듣기 | 2. Lv.0 과제 완료' );

-- Select Value
    -- All schedule
    SELECT * FROM schedule;
    -- Select some schedule
    SELECT * FROM schedule where id = 1;
    -- Select one user
    SELECT * FROM user where uid = '9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3';

-- Update Value
    -- Update schedule
    UPDATE schedule SET title = '11월 1일 데일리 스크럼!!' ,content = '1. Spring 강의 듣기 | 2. Lv.0 과제 완료 | 3. 코드 카타 진행하기' where id = 1;
    -- Update user
    UPDATE user SET name = '점숨염' WHERE id = '9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3';

-- Delete Value
    -- Delete schedule
    DELETE FROM schedule where id = 1;

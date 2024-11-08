CREATE TABLE user(
                     uid CHAR(36) NOT NULL PRIMARY KEY COMMENT '작성자 식별자',
                     name VARCHAR(30) NOT NULL COMMENT '작성자 이름',
                     password VARCHAR(100) NOT NULL COMMENT '작성자 비밀번호',
                     email VARCHAR(50) NOT NULL COMMENT '작성자 이메일',
                     created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '일정 작성 날짜',
                     updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '일정 수정 날짜'
);

CREATE TABLE schedule (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '일정 식별자',
                          uid CHAR(36) NOT NULL COMMENT '작성자 uid',
                          title VARCHAR(50) NOT NULL COMMENT '일정 제목',
                          content VARCHAR(100) NOT NULL COMMENT '일정 내용',
                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '일정 작성 날짜',
                          updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '일정 수정 날짜',
                          FOREIGN KEY (uid) REFERENCES user (uid)
);

INSERT INTO schedule (uid, title, content)
VALUES ('1523b4ab-08a1-49d5-bad8-b854514e873c','11월 1일 데일리 스크럼', '1. Spring 강의 듣기 | 2. Lv.0 과제 완료' );

INSERT INTO user (uid, name, password, email)
VALUES ('1523b4ab-08a1-49d5-bad8-b854514e873c', '숨염','qwer!@#$','sumyeom@gmail.com');

SELECT * FROM schedule;

SELECT * FROM schedule where id = 1;

SELECT * FROM user where uid = '1523b4ab-08a1-49d5-bad8-b854514e873c';

SELECT s.id, u.name, u.email, s.title, s.content, s.created_date, s.updated_date
FROM schedule s
JOIN user u ON s.uid = u.uid
AND (DATE(s.updated_date) = '2024-11-08' OR '2024-11-08' IS NULL)
AND (u.name = '숨염' OR '숨염' IS NULL
ORDER BY s.updated_date DESC;

SELECT s.id, u.name, u.email, s.title, s.content, s.created_date, s.updated_date
FROM schedule s
JOIN user u ON s.uid = u.uid
ORDER BY s.updated_date DESC
LIMIT 4 OFFSET 1 ;

UPDATE schedule SET title = '11월 1일 데일리 스크럼!!' ,content = '1. Spring 강의 듣기 | 2. Lv.0 과제 완료 | 3. 코드 카타 진행하기' where id = 1;

UPDATE user SET name = '점숨염' WHERE uid = '9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3';

DELETE FROM schedule where id = 1;

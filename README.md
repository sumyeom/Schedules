# 일정 관리 앱을 만들어보자!

Spring을 이용하여 일정을 관리할 수 있는 앱 서버 구현

---
### 요구 사항 기능
* Level 1
    * 일정 생성 : 할일, 작성자명, 비밀번호, 작성/수정일 포함하여 일정 생성
    * 전체 일정 조회 : 수정일 또는 작성자명 기준으로 조회하거나 전체 일정 리스트 출력
    * 선택 일정 조회 : 선택한 일정 출력

* Level 2
    * 선택 일정 수정 : 할일, 작성자명 수정, 수정 시에 비밀번호 전달하여 검사 후 수정
    * 선택 일정 삭제 : 삭제 시에 비밀번호 전달하여 검사 후 삭제

* Level 3
    * 작성자와 일정의 연결 : 이름으로만 판별하는 것이 아닌 따로 작성자를 관리하여 일정 구분

* Level 4
    * 페이지네이션 : 페이지 번호와 페이지 크기를 쿼리 파라미터로 전달 받아 리스트 출력

* Level 5
    * 예외 처리 : 에러 상황에 대한 예외처리

* Level 6
    * null 체크 및 특정 패턴에 대한 검증
 

---
#### Trouble Shooting
1. [Schedule Project](https://withsumyeom.tistory.com/entry/Trouble-Shooting-Schedule-Project)
---

## API 명세서

### Schedules

| 기능           | Method | URL                          | request    | response       | 상태 코드                                                    |
| -------------- | ------ | ---------------------------- | ---------- | -------------- | ------------------------------------------------------------ |
| 일정 생성      | POST   | /home/schedules              | 요청 body  | 등록 정보      | 200: 정상 등록 <br> 400 : 비정상적인 값                      |
| 전체 일정 조회 | GET    | /home/schedules              | 요청 param | 다건 응답 정보 | 200: 정상 조회 <br> 404 : 일정 찾을 수 없음                  |
| 선택 일정 조회 | GET    | /home/schedules/{scheduleId} | 요청 param | 단건 응답 정보 | 200: 정상 조회 <br> 404 : 일정 찾을 수 없음                  |
| 선택 일정 수정 | UPDATE | /home/schedules/{scheduleId} | 요청 body  | 수정 정보      | 200: 정상 등록 <br> 400 : 비정상적인 값 <br> 404 : 일정 찾을 수 없음 |
| 선택 일정 삭제 | DELETE | /home/schedules/{scheduleId} | 요청 body | -              | 200: 정상 등록 <br> 400 : 비정상적인 값 <br> 404 : 일정 찾을 수 없음                  |

<details>
  <summary>일정 생성</summary>


- 상세 정보

  <table>
    <tr>
    	<td>Method</td>
      <td>URL</td>
    </tr>
    <tr>
    	<td>POST</td>
      <td>/home/schedules</td>
    </tr>
  </table>

- Request : POST /home/schedules

  ```json
  {
    "name" : "숨염",
    "password" : "qwer!@#$",
    "email" : "sum@gmail.com"
    "title" : "11월 1일 데일리 스크럼",
    "content" : "1. Spring 강의 듣기 | 2. Lv.0 과제 완료"
  }
  ```

  - Request Elements

    <table>
      <tr>
      	<td>파라미터</td>
        <td>타입</td>
        <td>필수 여부</td>
        <td>설명</td>
      </tr>
      <tr>
      	<td>id</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 id</td>
      </tr>
      <tr>
      	<td>name</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이름</td>
      </tr>
      <tr>
      	<td>password</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 비밀번호</td>
      </tr>
      <tr>
      	<td>email</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이메일</td>
      </tr>
      <tr>
        <td>title</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 제목</td>
      </tr>
      <tr>
        <td>content</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 내용</td>
      </tr>


    </table>

- Response

  - 200 : 정상 등록

  ```plaintext
  HTTP/1.1 200 OK
  ```

  ```json
  {
    "scheduleId": 1
  }
  ```

  - 400 : 비정상적인 값

  ```plaintext
  HTTP/1.1 400 Bad Request
  ```

  ```json
  {
    "status": 400,
    "code": "INVALID_INPUT_VALUE",
    "message": "잘못된 입력값입니다.",
    "detailMessage": "[password] :비밀번호를 입력해주세요."
  }
  ```

  - Response Elements

    <table>
      <tr>
      	<td>파라미터</td>
        <td>타입</td>
        <td>필수 여부</td>
        <td>설명</td>
      </tr>
      <tr>
      	<td>scheduleId</td>
        <td>Integer</td>
        <td>필수</td>
        <td>일정 id</td>
      </tr>
    </table>

    </details>



<details>
  <summary>전체 일정 조회</summary>


- 상세 정보

  <table>
    <tr>
    	<td>Method</td>
      <td>URL</td>
    </tr>
    <tr>
    	<td>GET</td>
      <td>/home/schedules</td>
    </tr>
  </table>

- Request : GET /home/schedules

  ```plaintext
  - no prameter
  - name=수염
  - updatedDate=2024-11-08
  - name=수염&updatedDate=2024-11-08
  ```

- Response

  - 200 : 정상 조회

  ```plaintext
  HTTP/1.1 200 OK
  ```

  ```json
  [{
      "scheduleId": 1,
      "name": "수염",
      "email": "sum@gmail.com",
      "title": "11월 8일 데일리 스크럼1",
      "content": "1. Spring 강의 듣기 | 2. Lv.0 과제 완료",
      "createdDate": "2024-11-08T11:55:28",
      "updatedDate": "2024-11-08T11:55:28"
  },
  {
      "scheduleId": 2,
      "name": "수염이",
      "email": "yeom123@gmail.com",
      "title": "11월 12일 데일리 스크럼4",
      "content": "1. 복습",
      "createdDate": "2024-11-12T12:18:47",
      "updatedDate": "2024-11-12T12:18:47"
  }]
  ```

  - 404 : 일정 찾을 수 없음

  ```plaintext
  HTTP/1.1 404 Not Found
  ```

  ```json
  {
    "status": 404,
    "code": "SCHEDULE_NOT_FOUND",
    "message": "해당 일정을 찾을 수 없습니다.",
    "detailMessage": "해당 일정을 찾을 수 없습니다."
  }
  ```

  - Response Elements

    <table>
      <tr>
        <td>파라미터</td>
        <td>타입</td>
        <td>필수 여부</td>
        <td>설명</td>
      </tr>
      <tr>
        <td>scheduleId</td>
        <td>Integer</td>
        <td>필수</td>
        <td>일정 id</td>
      </tr>
      <tr>
        <td>name</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이름</td>
      </tr>
      <tr>
        <td>email</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이메일</td>
      </tr>
      <tr>
        <td>title</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 제목</td>
      </tr>
      <tr>
        <td>content</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 내용</td>
      </tr>
      <tr>
        <td>createDate</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 등록 날짜 timestamp</td>
      </tr>
      <tr>
        <td>updateDate</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 수정 날짜 timestamp</td>
      </tr>
    </table>

    </details>



<details>
  <summary>선택 일정 조회</summary>


- 상세 정보

  <table>
    <tr>
    	<td>Method</td>
      <td>URL</td>
    </tr>
    <tr>
    	<td>GET</td>
      <td>/home/schedules/{scheduleId}</td>
    </tr>
  </table>

- Request : GET /home/schedules/{scheduleId}

- Response

  - 200 : 정상 조회

  ```plaintext
  HTTP/1.1 200 OK
  ```

  ```json
  {
    "scheduleId": 15,
    "name": "수염ww",
    "email": "sumyeom221232@gmail.com",
    "title": "11월 8일",
    "content": "1. 코드카타",
    "createdDate": "2024-11-08T12:17:28",
    "updatedDate": "2024-11-08T12:17:28"
  }
  ```

  - 404 : 일정 찾을 수 없음

  ```plaintext
  HTTP/1.1 404 Not Found
  ```

  ```json
  {
    "status": 404,
    "code": "SCHEDULE_NOT_FOUND",
    "message": "해당 일정을 찾을 수 없습니다.",
    "detailMessage": "해당 일정을 찾을 수 없습니다."
  }
  ```

  - Response Elements

    <table>
      <tr>
        <td>파라미터</td>
        <td>타입</td>
        <td>필수 여부</td>
        <td>설명</td>
      </tr>
      <tr>
        <td>scheduleId</td>
        <td>Integer</td>
        <td>필수</td>
        <td>일정 id</td>
      </tr>
       <tr>
        <td>name</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이름</td>
      </tr>
      <tr>
        <td>email</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이메일</td>
      </tr>
      <tr>
        <td>title</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 제목</td>
      </tr>
      <tr>
        <td>content</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 내용</td>
      </tr>
      <tr>
        <td>createDate</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 등록 날짜 timestamp</td>
      </tr>
      <tr>
        <td>updateDate</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 수정 날짜 timestamp</td>
      </tr>
    </table>

    </details>


<details>
  <summary>선택 일정 수정</summary>


- 상세 정보

  <table>
    <tr>
    	<td>Method</td>
      <td>URL</td>
    </tr>
    <tr>
    	<td>PUT</td>
      <td>/home/schedules/{scheduleId}</td>
    </tr>
  </table>

- Request : PUT /home/schedules/{scheduleId}

  ```json
  {
    "userName" : "수염",
    "password" : "qwer!@#$2",
    "title" : "수정한 제목",
    "content" : "수정한 내용"
  }
  ```

  - Request Elements

    <table>
      <tr>
      	<td>파라미터</td>
        <td>타입</td>
        <td>필수 여부</td>
        <td>설명</td>
      </tr>
      <tr>
      	<td>name</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이름</td>
      </tr>
      <tr>
      	<td>password</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 비밀번호</td>
      </tr>
      <tr>
        <td>title</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 제목</td>
      </tr>
      <tr>
        <td>content</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 내용</td>
      </tr>


    </table>

- Response

  - 200 : 정상 수정

  ```plaintext
  HTTP/1.1 200 OK
  ```

  ```json
  {
    "scheduleId": 1
  }
  ```

  - 400 : 비정상적인 값

  ```plaintext
  HTTP/1.1 400 Bad Request
  ```

  ```json
  {
    "status": 400,
    "code": "INVALID_INPUT_VALUE",
    "message": "잘못된 입력값입니다.",
    "detailMessage": "[password] :비밀번호를 입력해주세요."
  }
  ```
  
  - 403 :  비밀번호 불일치
    
  ```plaintext
  HTTP/1.1 403 Forbidden
  ```

  ```json
  {
    "status": 403,
    "code": "INVALID_PASSWORD",
    "message": "비밀 번호가 일치하지 않습니다.",
    "detailMessage": "비밀 번호가 일치하지 않습니다."
  }
  ```

  - 404 : 일정 찾을 수 없음

  ```plaintext
  HTTP/1.1 404 Not Found
  ```

  ```json
  {
    "status": 404,
    "code": "SCHEDULE_NOT_FOUND",
    "message": "해당 일정을 찾을 수 없습니다.",
    "detailMessage": "해당 일정을 찾을 수 없습니다."
  }
  ```

  - Response Elements

    <table>
      <tr>
      	<td>파라미터</td>
        <td>타입</td>
        <td>필수 여부</td>
        <td>설명</td>
      </tr>
      <tr>
      	<td>scheduleId</td>
        <td>Integer</td>
        <td>필수</td>
        <td>일정 id</td>
      </tr>
    </table>

</details>

<details>
  <summary>선택 일정 삭제</summary>


- 상세 정보

 <table>
  <tr>
    <td>Method</td>
    <td>URL</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td>/home/schedules/{scheduleId}</td>
  </tr>
</table>


- Request : DELETE /home/schedules/{scheduleId}

  ```plaintext
  password=123
  ```

- Response

  - 200 : 정상 삭제

  ```plaintext
  HTTP/1.1 200 OK
  ```

  - 400 : 비정상적인 값
    
  ```plaintext
  HTTP/1.1 400 Bad Request
  ```

  ```json
  {
    "status": 400,
    "code": "INVALID_INPUT_VALUE",
    "message": "잘못된 입력값입니다.",
    "detailMessage": "[password] :비밀번호를 입력해주세요."
  }
  ```
  
  - 403 :  비밀번호 불일치
    
  ```plaintext
  HTTP/1.1 403 Forbidden
  ```

  ```json
  {
    "status": 403,
    "code": "INVALID_PASSWORD",
    "message": "비밀 번호가 일치하지 않습니다.",
    "detailMessage": "비밀 번호가 일치하지 않습니다."
  }
  ```
  
  
  - 404 : 일정 찾을 수 없음

  ```plaintext
  HTTP/1.1 404 Not Found
  ```

  ```json
  {
    "status": 404,
    "code": "SCHEDULE_NOT_FOUND",
    "message": "해당 일정을 찾을 수 없습니다.",
    "detailMessage": "해당 일정을 찾을 수 없습니다."
  }
  ```

  </details>

---

## ERD

![image](https://github.com/user-attachments/assets/e8cef1cb-cea5-41bc-9985-af96dafada66)

---

## SQL

- 테이블 생성

  - Scehdule table

  ~~~sql
  CREATE TABLE schedule (
        id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '일정 식별자',
        uid CHAR(36) NOT NULL COMMENT '작성자 uid',
        title VARCHAR(50) NOT NULL COMMENT '일정 제목',
        content VARCHAR(100) NOT NULL COMMENT '일정 내용',
        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '일정 작성 날짜',
        updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '일정 수정 날짜',
        FOREIGN KEY (uid) REFERENCES user (uid)
  )
  ~~~

  - User table

  ```sql
  CREATE TABLE user(
       uid CHAR(36) NOT NULL PRIMARY KEY COMMENT '작성자 식별자',
       name VARCHAR(30) NOT NULL COMMENT '작성자 이름',
       password VARCHAR(100) NOT NULL COMMENT '작성자 비밀번호',
       email VARCHAR(50) NOT NULL COMMENT '작성자 이메일',
       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '일정 작성 날짜',
       updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '일정 수정 날짜'
  )

  ```

- 값 삽입

  - Schedule table에 삽입

  ~~~sql
  INSERT INTO schedule (uid, title, content)
  VALUES ('1523b4ab-08a1-49d5-bad8-b854514e873c','11월 1일 데일리 스크럼', '1. Spring 강의 듣기 | 2. Lv.0 과제 완료' )
  ~~~

  - User table에 삽입

  ~~~sql
  INSERT INTO user (uid, name, password, email)
  VALUES ('1523b4ab-08a1-49d5-bad8-b854514e873c', '숨염','qwer!@#$','sumyeom@gmail.com')
  ~~~

- 값 조회

  - 전체 일정 조회

    ~~~sql
    SELECT * FROM schedule
    ~~~

  - 선택 일정 조회

    ~~~sql
    SELECT * FROM schedule where id = 1
    ~~~

  - 작성자 조회

    ~~~sql
    SELECT * FROM user where uid = '9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3'
    ~~~

  - user와 schedule 테이블에서 uid가 같은 값 중 schedule의 updated_date와 user의 name을 선택하여 조회
    ~~~sql
    SELECT s.id, u.name, u.email, s.title, s.content, s.created_date, s.updated_date
    FROM schedule s
    JOIN user u ON s.uid = u.uid
    AND (DATE(s.updated_date) = '2024-11-08' OR '2024-11-08' IS NULL)
    AND (u.name = '숨염' OR '숨염' IS NULL
    ORDER BY s.updated_date DESC;
    ~~~

  - 페이지 나누어서 조회
    ~~~sql
    SELECT s.id, u.name, u.email, s.title, s.content, s.created_date, s.updated_date
    FROM schedule s
    JOIN user u ON s.uid = u.uid
    ORDER BY s.updated_date DESC
    LIMIT 4 OFFSET 1 ;
    ~~~

- 값 수정

  - 일정 수정

    ~~~sql
    UPDATE schedule SET title = '11월 1일 데일리 스크럼!!' ,content = '1. Spring 강의 듣기 | 2. Lv.0 과제 완료 | 3. 코드 카타 진행하기' where id = 1
    ~~~

  - 작성자 수정

    ~~~sql
    UPDATE user SET name = '점숨염' WHERE id = '9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3'
    ~~~

- 값 삭제

  - 선택 일정 삭제

    ~~~sql
    DELETE FROM schedule where id = 1
    ~~~

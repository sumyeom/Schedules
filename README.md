# 일정 관리 앱을 만들어보자!
Spring을 이용하여 일정을 관리할 수 있는 앱 서버 구현

---

## API 명세서

### Schedules

| 기능           | Method | URL                            | request    | response       | 상태 코드                                                    |
| -------------- | ------ | ------------------------------ | ---------- | -------------- | ------------------------------------------------------------ |
| 일정 생성      | POST   | /home/schedules                | 요청 body  | 등록 정보      | 200: 정상 등록 <br> 400 : 비정상적인 값                      |
| 전체 일정 조회 | GET    | /home/schedules                | 요청 param | 다건 응답 정보 | 200: 정상 조회 <br> 404 : 일정 찾을 수 없음                  |
| 선택 일정 조회 | GET    | /home/schedules/{schedules_id} | 요청 param | 단건 응답 정보 | 200: 정상 조회 <br> 404 : 일정 찾을 수 없음                  |
| 선택 일정 수정 | UPDATE | /home/schedules/{schedules_id} | 요청 body  | 수정 정보      | 200: 정상 등록 <br> 400 : 비정상적인 값 <br> 404 : 일정 찾을 수 없음 |
| 선택 일정 삭제 | DELETE | /home/schedules/{schedules_id} | 요청 param | -              | 200: 정상 등록 <br> 404 : 일정 찾을 수 없음                  |

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
    "id" : "sumyeom",
    "name" : "숨염",
    "password" : "qwer!@#$",
    "title" : "11월 1일 데일리 스크럼",
    "content" : "1. Spring 강의 듣기 | 2. Lv.0 과제 완료",
    "group" : "Spring 프로젝트"
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
        <td>group</td>
        <td>String</td>
        <td>옵션</td>
        <td>일정 그룹</td>
      </tr>
      
    </table>
- Response
  - 200 : 정상 등록
  ```plaintext
  HTTP/1.1 200 OK
  ```
  ```json
  {
    "schedule_id": 1
  }
  ```
  - 400 : 비정상적인 값
  ```plaintext
  HTTP/1.1 400 Bad Request
  ```
  ```json
  {
    "error" : "Invalid input data",
    "message" : "The 'title' field is requied."
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
      	<td>scheduls_id</td>
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
  필요한 데이터는 Query parameter나 header로 전달
  ```
  
- Response
  - 200 : 정상 조회
  ```plaintext
  HTTP/1.1 200 OK
  ```
  ```json
  "schedule" : [
  {
    "schedule_id": 1,
    "user_id" : "9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3",
    "title" : "11월 1일 데일리 스크럼",
    "content" : "1. Spring 강의 듣기 | 2. Lv.0 과제 완료",
    "group" : "Spring 프로젝트",
    "created-date" : "2024-11-01 12:43:17",
    "updated-date" : "2024-11-01 13:13:17"
  },
  {
    "schedule_id": 2,
    "user_id" : "9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3",
    "title" : "카페가기",
    "content" : "스타벅스 프라푸치노 먹기",
    "group" : "여가 시간",
    "created-date" : "2024-11-01 09:43:17",
    "updated-date" : "2024-11-01 15:43:17"
  },
  ]
  ```
  - 404 : 일정 찾을 수 없음
  ```plaintext
  HTTP/1.1 404 Not Found
  ```
  ```json
  {
    "error" : "Resource not found",
    "message" : "Schedule with id 1 not found."
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
        <td>scheduls_id</td>
        <td>Integer</td>
        <td>필수</td>
        <td>일정 id</td>
      </tr>
      <tr>
        <td>user_id</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 uid</td>
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
        <td>group</td>
        <td>String</td>
        <td>옵션</td>
        <td>일정 그룹</td>
      </tr>
      <tr>
        <td>created-date</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 등록 날짜 timestamp</td>
      </tr>
      <tr>
        <td>updated-date</td>
        <td>String</td>
        <td>옵션</td>
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
      <td>/home/schedules/{schedules_id}</td>
    </tr>
  </table>
  
- Request : GET /home/schedules/{schedules_id}
  
  ```plaintext
  필요한 데이터는 Query parameter나 header로 전달
  ```
  
- Response
  - 200 : 정상 조회
  ```plaintext
  HTTP/1.1 200 OK
  ```
  ```json
  {
    "schedule_id": 1,
    "user_id" : "9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3",
    "title" : "11월 1일 데일리 스크럼",
    "content" : "1. Spring 강의 듣기 | 2. Lv.0 과제 완료",
    "group" : "Spring 프로젝트",
    "created-date" : "2024-11-01 12:43:17",
    "updated-date" : "2024-11-01 13:13:17"
  }
  ```
  - 404 : 일정 찾을 수 없음
  ```plaintext
  HTTP/1.1 404 Not Found
  ```
  ```json
  {
    "error" : "Resource not found",
    "message" : "Schedule with id 1 not found."
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
        <td>scheduls_id</td>
        <td>Integer</td>
        <td>필수</td>
        <td>일정 id</td>
      </tr>
      <tr>
        <td>user_id</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 uid</td>
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
        <td>group</td>
        <td>String</td>
        <td>옵션</td>
        <td>일정 그룹</td>
      </tr>
      <tr>
        <td>created-date</td>
        <td>String</td>
        <td>필수</td>
        <td>일정 등록 날짜 timestamp</td>
      </tr>
      <tr>
        <td>updated-date</td>
        <td>String</td>
        <td>옵션</td>
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
      <td>/home/schedules/{schedules_id}</td>
    </tr>
  </table>

- Request : PUT /home/schedules/{schedules_id}
  
  ```json
  {
    "name" : "숨염",
    "password" : "qwer!@#$",
    "titme" : "11월 1일 데일리 스크럼!",
    "content" : "1. Spring 강의 듣기 | 2. Lv.0 과제 완료 | 3. 코드 카타 진행하기",
    "group" : "Spring 프로젝트"
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
        <td>group</td>
        <td>String</td>
        <td>옵션</td>
        <td>일정 그룹</td>
      </tr>
      
    </table>
- Response
  - 200 : 정상 수정
  ```plaintext
  HTTP/1.1 200 OK
  ```
  ```json
  {
    "schedule_id": 1
  }
  ```
  - 400 : 비정상적인 값
  ```plaintext
  HTTP/1.1 400 Bad Request
  ```
  ```json
  {
    "error" : "Invalid input data",
    "message" : "The 'title' field is requied."
  }
  ```
  - 404 : 일정 찾을 수 없음
  ```plaintext
  HTTP/1.1 404 Not Found
  ```
  ```json
  {
    "error" : "Resource not found",
    "message" : "Schedule with id 1 not found."
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
      	<td>scheduls_id</td>
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
    <td>/home/schedules/{schedules_id}</td>
  </tr>
</table>

- Request : DELETE /home/schedules/{scehdules_id}
  
  ```plaintext
  필요한 데이터는 Query parameter나 header로 전달
  ```
- Response
  - 200 : 정상 삭제
  ```plaintext
  HTTP/1.1 200 OK
  ```
  - 404 : 일정 찾을 수 없음
  ```plaintext
  HTTP/1.1 404 Not Found
  ```
  ```json
  {
    "error" : "Resource not found",
    "message" : "Schedule with id 1 not found."
  }
  ```
</details>

### Users

| 기능           | Method | URL                            | request    | response       | 상태 코드                                                    |
| -------------- | ------ | ------------------------------ | ---------- | -------------- | ------------------------------------------------------------ |
| 작성자 등록      | POST   | /home/users                | 요청 body  | 등록 정보      | 200: 정상 등록 <br> 400 : 비정상적인 값                      |
| 작성자 수정 | UPDATE    | /home/users/{users_id}                | 요청 body | 다건 응답 정보 | 200: 정상 등록 <br> 400 : 비정상적인 값 <br> 404 : 작성자 찾을 수 없음                 |
| 작성자 조회 | GET    | /home/users/{users_id} | 요청 param | 단건 응답 정보 | 200: 정상 조회 <br> 404 : 작성자 찾을 수 없음                  |


<details>
  <summary>작성자 등록</summary>
  
- 상세 정보
  
  <table>
    <tr>
    	<td>Method</td>
      <td>URL</td>
    </tr>
    <tr>
    	<td>POST</td>
      <td>/home/users</td>
    </tr>
  </table>

- Request : POST /home/users
  
  ```json
  {
    "id" : "sumyeom",
    "name" : "숨염",
    "password" : "qwer!@#$",
    "email" : "sumyeom@gmail.com"
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
    </table>
    
- Response
  - 200 : 정상 등록
  ```plaintext
  HTTP/1.1 200 OK
  ```
  ```json
  {
    "users_id": "9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3"
  }
  ```
  - 400 : 비정상적인 값
  ```plaintext
  HTTP/1.1 400 Bad Request
  ```
  ```json
  {
    "error" : "Invalid input data",
    "message" : "The 'name' field is requied."
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
      	<td>users_id</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 uid</td>
      </tr>
    </table>
</details>

<details>
  <summary>작성자 수정</summary>

- 상세 정보
  <table>
    <tr>
      <td>Method</td>
      <td>URL</td>
    </tr>
    <tr>
      <td>PUT</td>
      <td>/home/users/{users_id}</td>
    </tr>
  </table>

- Request : PUT /home/users/{users_id}
  
  ```json
  {
    "name" : "점숨염",
    "email" : "sumyeom1234@gmail.com"
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
        <td>email</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이메일</td>
      </tr>
    </table>
    
- Response
  - 200 : 정상 수정
  ```plaintext
  HTTP/1.1 200 OK
  ```
  ```json
  {
    "users_id": "9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3"
  }
  ```
  - 400 : 비정상적인 값
  ```plaintext
  HTTP/1.1 400 Bad Request
  ```
  ```json
  {
    "error" : "Invalid input data",
    "message" : "The 'name' field is requied."
  }
  ```
  - 404 : 작성자 찾을 수 없음
  ```plaintext
  HTTP/1.1 404 Not Found
  ```
  ```json
  {
    "error" : "Resource not found",
    "message" : "Users with id 1 not found."
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
        <td>users_id</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 uid</td>
      </tr>
    </table>

</details>


<details>
  <summary>작성자 조회</summary>
  
- 상세 정보
  
  <table>
    <tr>
    	<td>Method</td>
      <td>URL</td>
    </tr>
    <tr>
    	<td>GET</td>
      <td>/home/users/{users_id}</td>
    </tr>
  </table>
  
- Request : GET /home/users/{users_id}
  
  ```plaintext
  필요한 데이터는 Query parameter나 header로 전달
  ```
  
- Response
  - 200 : 정상 조회
  ```plaintext
  HTTP/1.1 200 OK
  ```
  ```json
  {
    "id" : "sumyeom",
    "name" : "점숨염",
    "email" : "sumyeom1234@gmail.com"
  }
  ```
  - 404 : 작성자 찾을 수 없음
  ```plaintext
  HTTP/1.1 404 Not Found
  ```
  ```json
  {
    "error" : "Resource not found",
    "message" : "User not found."
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
        <td>email</td>
        <td>String</td>
        <td>필수</td>
        <td>작성자 이메일</td>
      </tr>
    </table>
</details>

---
## ERD
![image](https://github.com/user-attachments/assets/51c59965-9b58-4c22-b299-6cd3f60e18d3)

---
## SQL
- 테이블 생성
  - Scehdule table
  ~~~sql
  CREATE TABLE schedule (
     id INT PRIMARY KEY AUTO_INCREMENT,
     user_id CHAR(36) NOT NULL,
       title VARCHAR(50) NOT NULL,
       content VARCHAR(100) NOT NULL,
       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
       updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       group_name VARCHAR(30),
       FOREIGN KEY (user_id) REFERENCES user (uid)
   )
  ~~~

  - User table
  ```sql
  CREATE TABLE user(
        uid CHAR(36) NOT NULL PRIMARY KEY ,
        id VARCHAR(30) NOT NULL ,
        name VARCHAR(30) NOT NULL,
        password VARCHAR(100) NOT NULL,
        email VARCHAR(50) NOT NULL,
        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   )
  ```

- 값 삽입
  - Schedule table에 삽입
  ~~~sql
  INSERT INTO schedule (user_id, title, content)
  VALUES ('9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3','11월 1일 데일리 스크럼', '1. Spring 강의 듣기 | 2. Lv.0 과제 완료' )
  ~~~

  - User table에 삽입
  ~~~sql
  INSERT INTO user (uid, id, name, password, email)
  VALUES (UUID(), 'sumyeom', '숨염','qwer!@#$','sumyeom@gmail.com')
  ~~~
- 값 조회
  - 전체 일정 조회
    ~~~sql
    SELECT * FROM schedule;
    ~~~
  
  - 선택 일정 조회
    ~~~sql
    SELECT * FROM schedule where id = 1
    ~~~
  
  - 작성자 조회
    ~~~sql
    SELECT * FROM user where uid = '9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3';
    ~~~

- 값 수정
  - 일정 수정
    ~~~sql
    UPDATE schedule SET title = '11월 1일 데일리 스크럼!!' ,content = '1. Spring 강의 듣기 | 2. Lv.0 과제 완료 | 3. 코드 카타 진행하기' where id = 1;
    ~~~
  
  - 작성자 수정
    ~~~sql
    UPDATE user SET name = '점숨염' WHERE id = '9ccb2fd0-97af-11ef-82ae-0fc9c3770cd3';
    ~~~

- 값 삭제
  - 선택 일정 삭제
    ~~~sql
    DELETE FROM schedule where id = 1
    ~~~

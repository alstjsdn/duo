# DuoDB

Spring Boot 프로젝트

## 기술 스택

- Java 17
- Spring Boot 4.0.3
- Spring Data JPA
- Spring Security
- MySQL
- Lombok
- Gradle

## 설정 방법

### 1. 데이터베이스 설정

MySQL에 데이터베이스를 생성합니다:
```sql
CREATE DATABASE duoDB;
```

### 2. Application 설정

`src/main/resources/application.yml.example` 파일을 복사하여 `application.yml` 파일을 만들고, 데이터베이스 정보를 입력합니다:

```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

`application.yml` 파일에서 다음 정보를 수정:
- `url`: MySQL 접속 주소
- `username`: MySQL 사용자명
- `password`: MySQL 비밀번호

### 3. 실행

```bash
./gradlew bootRun
```

## 주의사항

`application.yml` 파일은 보안상의 이유로 Git에 포함되지 않습니다. 프로젝트를 clone한 후 반드시 직접 생성해야 합니다.
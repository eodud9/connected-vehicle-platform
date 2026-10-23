# Connected Vehicle Backend Platform

Connected Vehicle 환경에서 발생하는 차량 데이터를 수집·처리하고, 차량의 현재 상태·운행 이력·이상 이벤트를 관리하는 Java/Spring 기반 Vehicle-to-Cloud Backend Platform입니다.

이 프로젝트는 기능을 한 번에 확장하기보다 **단순한 동기식 구조에서 시작해 실제 부하를 측정하고, 병목을 확인한 뒤 필요한 기술만 단계적으로 도입하는 과정**에 초점을 둡니다.

## Project Goals

- 다수의 가상 차량에서 발생하는 Telemetry 데이터를 안정적으로 수집
- 차량의 최신 상태와 운행 세션을 관리
- 배터리 저하, 고온, 급가속, 급감속 등의 이상 이벤트 감지
- 사용자와 차량 인증 체계를 분리하여 접근 제어
- 데이터 증가에 따른 병목을 측정하고 DB 최적화, 비동기 처리, Kafka, Redis 등의 도입 효과를 정량적으로 비교
- Docker, AWS, CI/CD까지 확장하여 실제 백엔드 운영 구조를 경험

## Architecture

현재 V1은 의도적으로 단순한 구조에서 시작합니다.

```text
Vehicle Simulator
      |
      | REST / Telemetry
      v
Spring Boot
      |
      v
    MySQL
```

초기에는 Spring Boot + MySQL 기반의 동기식 처리 구조를 사용합니다.

이후 Vehicle Simulator로 차량 수와 데이터 발생량을 증가시키며 TPS, latency, error rate, DB connection 등의 지표를 측정하고, 실제로 확인된 병목에 대해서만 아키텍처를 확장할 예정입니다.

## Core Domain

### Vehicle
- VIN, 번호판, 모델 정보 관리
- 차량별 API Key 기반 인증

### Telemetry
- 위치
- 속도
- 배터리 잔량
- 배터리 온도
- 시동 상태
- 차량에서 기록된 시각

### DrivingSession
Telemetry의 ignition 상태 전환을 기반으로 운행 세션을 생성하고 종료합니다.

```text
OFF -> ON   : DrivingSession 시작
ON  -> ON   : 기존 Session 유지
ON  -> OFF  : DrivingSession 종료
```

운행 종료 시 GPS 좌표를 기반으로 이동 거리를 계산하고 세션 평균 속도를 산출합니다.

### VehicleEvent

현재 다음 이벤트를 감지합니다.

- LOW_BATTERY
- HIGH_TEMPERATURE
- RAPID_ACCELERATION
- RAPID_DECELERATION

동일 상태가 지속될 때 이벤트가 반복 생성되지 않도록 임계값 진입 시점을 기준으로 처리합니다.

## Authentication & Authorization

사람과 차량의 인증 목적이 다르기 때문에 인증 체계를 분리했습니다.

### User Authentication

```text
email / password
        |
      BCrypt
        |
       JWT
```

- Spring Security
- JWT 기반 Stateless 인증
- USER / ADMIN 역할 관리
- USER는 현재 Ownership을 가진 차량만 접근 가능
- ADMIN은 전체 차량 조회 가능

### Vehicle Authentication

```text
Vehicle
   |
X-Vehicle-Api-Key
   |
SHA-256 Hash Comparison
   |
Telemetry API
```

차량 등록 시 256-bit API Key를 생성하고 원본은 최초 한 번만 반환합니다. 서버에는 SHA-256 해시만 저장하며 차량의 고빈도 Telemetry 요청을 인증합니다.

## Current APIs

### User / Auth

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/users` | 회원 가입 |
| POST | `/api/auth/login` | 로그인 및 JWT 발급 |

### Vehicle

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/vehicles` | 차량 등록 |
| GET | `/api/vehicles` | 접근 가능한 차량 목록 조회 |
| GET | `/api/vehicles/{vehicleId}` | 차량 정보 조회 |
| GET | `/api/vehicles/{vehicleId}/status` | 차량의 최신 상태 조회 |

### Telemetry

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/vehicles/{vehicleId}/telemetry` | 차량 Telemetry 수집 |
| GET | `/api/vehicles/{vehicleId}/telemetry?page=0&size=20` | Telemetry 이력 페이지 조회 |

### Driving Session

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/vehicles/{vehicleId}/driving-sessions?page=0&size=20` | 운행 세션 이력 페이지 조회 |

## Pagination

Telemetry와 DrivingSession은 시간이 지날수록 데이터가 계속 증가하기 때문에 전체 데이터를 한 번에 반환하지 않고 Spring Data JPA의 `Pageable`을 이용해 페이지 단위로 조회합니다.

예시:

```http
GET /api/vehicles/1/telemetry?page=0&size=20
GET /api/vehicles/1/driving-sessions?page=0&size=20
```

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.1.1
- Spring MVC
- Spring Data JPA
- Spring Security
- JWT (JJWT 0.13.0)
- Bean Validation

### Database
- MySQL

### Build
- Gradle

## Development Strategy

이 프로젝트의 핵심은 특정 기술을 먼저 넣는 것이 아니라 **문제를 측정한 뒤 해결책을 선택하는 것**입니다.

```text
1. Simple REST + MySQL baseline
           |
2. Vehicle Simulator
           |
3. Load 증가
           |
4. TPS / P95 / P99 / Error / DB 지표 측정
           |
5. Bottleneck 분석
           |
6. 해결 방법 비교
           |
7. 필요한 기술 도입
           |
8. 동일 부하에서 Before / After 검증
```

## Planned Improvements

아래 기술은 미리 적용하지 않고 실제 병목과 요구사항이 확인되는 시점에 도입 여부를 결정합니다.

- Vehicle Simulator
- DB Index / Query Optimization
- Kafka 기반 비동기 Telemetry 처리
- Redis 기반 최신 차량 상태 캐싱
- WebSocket 기반 실시간 상태 전달
- Docker / Docker Compose
- AWS Deployment
- GitHub Actions CI/CD
- Monitoring & Performance Test
- Vehicle Certificate / PKI / mTLS 연구

## Current Limitations

V1은 학습과 성능 비교를 위한 기준 구조이기 때문에 일부 제약을 의도적으로 가지고 있습니다.

- 차량별 Telemetry가 `recordedAt` 순서대로 도착한다고 가정
- 동일 차량의 동시 Telemetry 요청에 대한 별도 동시성 제어 없음
- 운행 종료 시 세션 전체 Telemetry를 다시 조회해 거리 계산
- 차량 인증은 API Key 기반이며 PKI/mTLS는 아직 적용하지 않음

이 제약들은 향후 부하 테스트와 설계 개선 과정에서 실제 문제로 관찰하고 개선할 대상입니다.

## Project Direction

최종적으로는 단순 CRUD 프로젝트가 아니라 다음 질문에 답할 수 있는 백엔드 프로젝트를 목표로 합니다.

> 차량 수와 데이터 발생량이 증가했을 때 어디에서 병목이 발생하는가?

> 그 병목을 해결하기 위해 왜 Kafka, Redis 또는 DB 최적화를 선택했는가?

> 개선 전후 성능은 실제로 얼마나 달라졌는가?

도메인은 Connected Vehicle이지만, 프로젝트에서 다루는 핵심 문제는 지속적으로 증가하는 데이터를 처리하는 백엔드 시스템의 **인증, 데이터 모델링, 트랜잭션, 성능, 확장성, 운영**입니다.

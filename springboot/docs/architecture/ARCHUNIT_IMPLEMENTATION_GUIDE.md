# ArchUnit 아키텍처 테스트 구현 가이드

## 목차
1. [개요](#1-개요)
2. [ArchUnit이란?](#2-archunit이란)
3. [현재 프로젝트 아키텍처](#3-현재-프로젝트-아키텍처)
4. [테스트 규칙 설계](#4-테스트-규칙-설계)
5. [구현 상세](#5-구현-상세)
6. [적용 방법](#6-적용-방법)
7. [기대 효과](#7-기대-효과)

---

## 1. 개요

### 1.1 문제 정의
현재 프로젝트는 헥사고날 아키텍처(Hexagonal Architecture)를 적용하고 있으나, 아키텍처 규칙 준수 여부를 자동으로 검증하는 메커니즘이 없습니다.

| 문제점 | 영향 |
|--------|------|
| 아키텍처 규칙 위반 시 코드 리뷰에서만 발견 | 휴먼 에러로 인한 위반 누락 가능 |
| 레이어 간 의존성 규칙 강제 불가 | 시간이 지남에 따라 아키텍처 붕괴 |
| 신규 개발자 온보딩 시 규칙 전달 어려움 | 일관성 없는 코드 작성 |

### 1.2 해결 방안
ArchUnit을 도입하여 **컴파일 타임**에 아키텍처 규칙 위반을 자동으로 감지합니다.

---

## 2. ArchUnit이란?

### 2.1 정의
ArchUnit은 Java 코드의 아키텍처를 테스트할 수 있는 오픈소스 라이브러리입니다. JUnit과 통합되어 일반 단위 테스트처럼 실행할 수 있습니다.

### 2.2 주요 기능

| 기능 | 설명 |
|------|------|
| **패키지 의존성 검사** | 특정 패키지가 다른 패키지에 의존하지 않도록 강제 |
| **클래스 의존성 검사** | 클래스 간 의존 관계 규칙 정의 |
| **네이밍 컨벤션 검사** | 클래스명, 패키지명 규칙 강제 |
| **어노테이션 검사** | 특정 어노테이션 사용 강제 |
| **상속 관계 검사** | 특정 클래스/인터페이스 상속 강제 |
| **레이어 검사** | 레이어 간 의존성 방향 검증 |
| **사이클 검사** | 순환 의존성 탐지 |

### 2.3 작동 원리

```
┌─────────────────────────────────────────────────────────────┐
│                    ArchUnit 동작 흐름                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. 클래스 로딩                                               │
│     ├── @AnalyzeClasses 어노테이션으로 분석 대상 지정            │
│     └── 바이트코드를 분석하여 클래스 메타데이터 추출             │
│                                                             │
│  2. 규칙 정의                                                 │
│     ├── Fluent API로 아키텍처 규칙 작성                        │
│     └── @ArchTest 어노테이션으로 테스트 메서드 지정             │
│                                                             │
│  3. 규칙 검증                                                 │
│     ├── 정의된 규칙을 클래스들에 적용                          │
│     └── 위반 사항 수집                                        │
│                                                             │
│  4. 결과 보고                                                 │
│     ├── 위반 시 AssertionError 발생 → 테스트 실패             │
│     └── 위반 내용 상세 출력                                   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 3. 현재 프로젝트 아키텍처

### 3.1 헥사고날 아키텍처 구조

```
com.mzc.backend.lms.domains.{도메인명}/
├── adapter/
│   ├── in/
│   │   └── web/                    # Driving Adapter (Controller)
│   │       ├── *Controller.java
│   │       └── dto/
│   │           ├── request/        # Request DTOs
│   │           └── response/       # Response DTOs
│   └── out/
│       ├── persistence/            # Driven Adapter (Repository 구현체)
│       │   ├── repository/         # JPA Repository
│       │   └── entity/             # JPA Entity
│       └── external/               # 외부 시스템 Adapter
│
├── application/
│   ├── port/
│   │   ├── in/                     # Driving Port (UseCase 인터페이스)
│   │   │   └── *UseCase.java
│   │   └── out/                    # Driven Port (Repository Port 인터페이스)
│   │       └── *RepositoryPort.java
│   └── service/                    # Service 구현체
│       └── *Service.java
│
├── domain/                         # 도메인 모델
│   └── *.java
│
└── exception/                      # 도메인 예외
    └── *Exception.java
```

### 3.2 도메인 목록

| 도메인 | 설명 | 패키지 |
|--------|------|--------|
| user | 사용자 관리 | `domains.user` |
| course | 강의 관리 | `domains.course` |
| enrollment | 수강 신청 | `domains.enrollment` |
| board | 게시판 | `domains.board` |
| assessment | 평가 | `domains.assessment` |
| attendance | 출석 | `domains.attendance` |
| notification | 알림 | `domains.notification` |
| message | 메시지 | `domains.message` |
| dashboard | 대시보드 | `domains.dashboard` |
| academy | 학원 관리 | `domains.academy` |

### 3.3 의존성 규칙

헥사고날 아키텍처에서 의존성은 항상 **외부에서 내부로** 향해야 합니다.

```
┌─────────────────────────────────────────────────────────────────┐
│                    의존성 방향 (Outside → Inside)                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│    [Web Adapter]  ──────→  [UseCase Port]  ←──────  [Service]   │
│    (Controller)            (Interface)               (Impl)     │
│         │                       │                       │       │
│         │                       │                       │       │
│         └───────────────────────┼───────────────────────┘       │
│                                 │                               │
│                                 ▼                               │
│                         [Domain Model]                          │
│                            (Entity)                             │
│                                 │                               │
│                                 ▼                               │
│    [Repository]  ────→  [Repository Port]  ←────  [Persistence] │
│    (JPA)               (Interface)               (Adapter)      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

※ 금지된 의존성:
  ✗ Domain → Adapter
  ✗ Port → Adapter
  ✗ Service → Controller
  ✗ Domain A → Domain B (도메인 간 직접 참조)
```

---

## 4. 테스트 규칙 설계

### 4.1 테스트 규칙 분류

| 분류 | 규칙 ID | 설명 | 우선순위 |
|------|---------|------|----------|
| **헥사고날 아키텍처** | HEX-001 | 도메인 레이어는 외부 의존성 금지 | P1 |
| | HEX-002 | Application Port는 Adapter에 의존 금지 | P1 |
| | HEX-003 | Service는 UseCase Port 구현 필수 | P1 |
| | HEX-004 | Adapter는 Port를 통해서만 Application에 접근 | P1 |
| **도메인 경계** | DOM-001 | 도메인 간 직접 의존 금지 | P1 |
| | DOM-002 | 공통 모듈만 크로스 도메인 참조 허용 | P2 |
| **네이밍 컨벤션** | NAM-001 | Controller는 @RestController + *Controller | P2 |
| | NAM-002 | Service는 @Service + *Service | P2 |
| | NAM-003 | UseCase는 *UseCase 접미사 | P2 |
| | NAM-004 | Repository Port는 *RepositoryPort 접미사 | P2 |
| **어노테이션** | ANN-001 | Entity는 @Entity 필수 | P3 |
| | ANN-002 | Request DTO는 Validation 어노테이션 필수 | P3 |
| **패키지 구조** | PKG-001 | 도메인별 표준 패키지 구조 준수 | P2 |

### 4.2 상세 규칙 정의

#### HEX-001: 도메인 레이어 독립성

```
도메인 레이어 (domain 패키지)가 의존할 수 있는 패키지:
  ✓ java.* (JDK 기본 클래스)
  ✓ lombok.* (어노테이션)
  ✓ jakarta.persistence.* (JPA 어노테이션만, 구현체 X)

  ✗ org.springframework.* (Spring Framework)
  ✗ com.mzc.backend.lms.domains.*.adapter.* (Adapter)
  ✗ com.mzc.backend.lms.domains.*.application.* (Application)
```

#### HEX-002: Port 독립성

```
Application Port (port 패키지)가 의존할 수 있는 패키지:
  ✓ java.*
  ✓ com.mzc.backend.lms.domains.*.domain.* (Domain Model)
  ✓ com.mzc.backend.lms.domains.*.adapter.in.web.dto.* (DTO)
  ✓ org.springframework.data.domain.* (Pageable, Page 등)

  ✗ com.mzc.backend.lms.domains.*.adapter.out.* (Out Adapter)
  ✗ com.mzc.backend.lms.domains.*.application.service.* (Service 구현체)
```

#### DOM-001: 도메인 간 격리

```
도메인 A가 도메인 B를 직접 참조 금지:
  ✗ domains.user → domains.course
  ✗ domains.course → domains.enrollment

예외 (허용):
  ✓ 공통 모듈 참조: domains.* → common.*
  ✓ 공유 이벤트: domains.* → *.domain.event.*
```

---

## 5. 구현 상세

### 5.1 의존성 추가

**build.gradle**
```groovy
dependencies {
    // ArchUnit
    testImplementation 'com.tngtech.archunit:archunit-junit5:1.2.1'
}
```

### 5.2 테스트 클래스 구조

```
src/test/java/com/mzc/backend/lms/
└── architecture/
    ├── HexagonalArchitectureTest.java    # 헥사고날 아키텍처 규칙
    ├── DomainBoundaryTest.java           # 도메인 경계 규칙
    ├── NamingConventionTest.java         # 네이밍 컨벤션 규칙
    └── PackageStructureTest.java         # 패키지 구조 규칙
```

### 5.3 헥사고날 아키텍처 테스트 (HexagonalArchitectureTest.java)

```java
package com.mzc.backend.lms.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * 헥사고날 아키텍처 규칙 테스트
 *
 * 의존성 방향: Adapter → Port → Domain (외부 → 내부)
 */
@AnalyzeClasses(
    packages = "com.mzc.backend.lms.domains",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class HexagonalArchitectureTest {

    // ============================================================
    // HEX-001: 도메인 레이어는 외부에 의존하지 않음
    // ============================================================

    @ArchTest
    static final ArchRule domain_should_not_depend_on_adapter =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter..");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_application =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..application..");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_spring =
        noClasses()
            .that().resideInAPackage("..domain..")
            .and().haveSimpleNameNotEndingWith("Entity") // JPA Entity는 예외
            .should().dependOnClassesThat()
            .resideInAPackage("org.springframework..");

    // ============================================================
    // HEX-002: Application Port는 Adapter에 의존하지 않음
    // ============================================================

    @ArchTest
    static final ArchRule port_in_should_not_depend_on_adapter =
        noClasses()
            .that().resideInAPackage("..application.port.in..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter.out..");

    @ArchTest
    static final ArchRule port_out_should_not_depend_on_adapter =
        noClasses()
            .that().resideInAPackage("..application.port.out..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter..");

    // ============================================================
    // HEX-003: Service는 Port를 통해 Adapter와 통신
    // ============================================================

    @ArchTest
    static final ArchRule service_should_not_depend_on_repository_impl =
        noClasses()
            .that().resideInAPackage("..application.service..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter.out.persistence.repository..");

    @ArchTest
    static final ArchRule service_should_not_depend_on_controller =
        noClasses()
            .that().resideInAPackage("..application.service..")
            .should().dependOnClassesThat()
            .resideInAPackage("..adapter.in.web..")
            .andShould().haveSimpleNameEndingWith("Controller");

    // ============================================================
    // HEX-004: Controller는 UseCase를 통해서만 Service 접근
    // ============================================================

    @ArchTest
    static final ArchRule controller_should_only_use_use_case =
        classes()
            .that().resideInAPackage("..adapter.in.web..")
            .and().haveSimpleNameEndingWith("Controller")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..adapter.in.web..",           // 같은 패키지 (DTO 포함)
                "..application.port.in..",      // UseCase Port만
                "..common..",                   // 공통 모듈
                "java..",                       // Java 기본
                "jakarta..",                    // Jakarta (validation 등)
                "lombok..",                     // Lombok
                "org.springframework..",        // Spring Framework
                "org.slf4j.."                   // Logging
            );
}
```

### 5.4 도메인 경계 테스트 (DomainBoundaryTest.java)

```java
package com.mzc.backend.lms.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * 도메인 경계 규칙 테스트
 *
 * 각 도메인은 독립적이어야 하며, 다른 도메인에 직접 의존하면 안 됨
 */
@AnalyzeClasses(
    packages = "com.mzc.backend.lms.domains",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class DomainBoundaryTest {

    // ============================================================
    // DOM-001: 도메인 간 순환 의존성 금지
    // ============================================================

    @ArchTest
    static final ArchRule domains_should_be_free_of_cycles =
        SlicesRuleDefinition.slices()
            .matching("com.mzc.backend.lms.domains.(*)..")
            .should().beFreeOfCycles();

    // ============================================================
    // DOM-002: 특정 도메인 간 직접 의존 금지
    // ============================================================

    @ArchTest
    static final ArchRule user_domain_should_not_depend_on_course =
        noClasses()
            .that().resideInAPackage("..domains.user..")
            .should().dependOnClassesThat()
            .resideInAPackage("..domains.course..");

    @ArchTest
    static final ArchRule course_domain_should_not_depend_on_user_internal =
        noClasses()
            .that().resideInAPackage("..domains.course..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "..domains.user.application.service..",
                "..domains.user.adapter.."
            );

    @ArchTest
    static final ArchRule board_domain_should_not_depend_on_other_domains =
        noClasses()
            .that().resideInAPackage("..domains.board..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "..domains.user..",
                "..domains.course..",
                "..domains.enrollment..",
                "..domains.assessment..",
                "..domains.notification.."
            );
}
```

### 5.5 네이밍 컨벤션 테스트 (NamingConventionTest.java)

```java
package com.mzc.backend.lms.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * 네이밍 컨벤션 규칙 테스트
 */
@AnalyzeClasses(
    packages = "com.mzc.backend.lms.domains",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class NamingConventionTest {

    // ============================================================
    // NAM-001: Controller 네이밍 규칙
    // ============================================================

    @ArchTest
    static final ArchRule controllers_should_be_suffixed =
        classes()
            .that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Controller")
            .because("REST 컨트롤러는 'Controller' 접미사를 가져야 합니다");

    @ArchTest
    static final ArchRule controllers_should_reside_in_web_package =
        classes()
            .that().haveSimpleNameEndingWith("Controller")
            .should().resideInAPackage("..adapter.in.web..")
            .because("컨트롤러는 adapter.in.web 패키지에 위치해야 합니다");

    // ============================================================
    // NAM-002: Service 네이밍 규칙
    // ============================================================

    @ArchTest
    static final ArchRule services_should_be_suffixed =
        classes()
            .that().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("Service")
            .because("서비스 클래스는 'Service' 접미사를 가져야 합니다");

    @ArchTest
    static final ArchRule services_should_reside_in_service_package =
        classes()
            .that().haveSimpleNameEndingWith("Service")
            .and().areAnnotatedWith(Service.class)
            .should().resideInAPackage("..application.service..")
            .because("서비스는 application.service 패키지에 위치해야 합니다");

    // ============================================================
    // NAM-003: UseCase 네이밍 규칙
    // ============================================================

    @ArchTest
    static final ArchRule use_cases_should_be_suffixed =
        classes()
            .that().resideInAPackage("..application.port.in..")
            .and().areInterfaces()
            .should().haveSimpleNameEndingWith("UseCase")
            .because("UseCase 인터페이스는 'UseCase' 접미사를 가져야 합니다");

    // ============================================================
    // NAM-004: Repository Port 네이밍 규칙
    // ============================================================

    @ArchTest
    static final ArchRule repository_ports_should_be_suffixed =
        classes()
            .that().resideInAPackage("..application.port.out..")
            .and().areInterfaces()
            .should().haveSimpleNameEndingWith("RepositoryPort")
            .orShould().haveSimpleNameEndingWith("Port")
            .because("Repository Port는 'RepositoryPort' 또는 'Port' 접미사를 가져야 합니다");

    // ============================================================
    // NAM-005: DTO 네이밍 규칙
    // ============================================================

    @ArchTest
    static final ArchRule request_dtos_should_be_suffixed =
        classes()
            .that().resideInAPackage("..dto.request..")
            .should().haveSimpleNameEndingWith("Request")
            .orShould().haveSimpleNameEndingWith("RequestDto")
            .because("Request DTO는 'Request' 또는 'RequestDto' 접미사를 가져야 합니다");

    @ArchTest
    static final ArchRule response_dtos_should_be_suffixed =
        classes()
            .that().resideInAPackage("..dto.response..")
            .should().haveSimpleNameEndingWith("Response")
            .orShould().haveSimpleNameEndingWith("ResponseDto")
            .because("Response DTO는 'Response' 또는 'ResponseDto' 접미사를 가져야 합니다");
}
```

### 5.6 패키지 구조 테스트 (PackageStructureTest.java)

```java
package com.mzc.backend.lms.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * 패키지 구조 규칙 테스트
 */
@AnalyzeClasses(
    packages = "com.mzc.backend.lms.domains",
    importOptions = ImportOption.DoNotIncludeTests.class
)
public class PackageStructureTest {

    // ============================================================
    // PKG-001: Entity 위치 규칙
    // ============================================================

    @ArchTest
    static final ArchRule entities_should_reside_in_entity_package =
        classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("..adapter.out.persistence.entity..")
            .orShould().resideInAPackage("..domain..")
            .because("JPA Entity는 persistence.entity 또는 domain 패키지에 위치해야 합니다");

    // ============================================================
    // PKG-002: Repository 위치 규칙
    // ============================================================

    @ArchTest
    static final ArchRule jpa_repositories_should_reside_in_repository_package =
        classes()
            .that().areAssignableTo(JpaRepository.class)
            .should().resideInAPackage("..adapter.out.persistence.repository..")
            .because("JPA Repository는 persistence.repository 패키지에 위치해야 합니다");

    // ============================================================
    // PKG-003: Exception 위치 규칙
    // ============================================================

    @ArchTest
    static final ArchRule exceptions_should_reside_in_exception_package =
        classes()
            .that().haveSimpleNameEndingWith("Exception")
            .and().areAssignableTo(RuntimeException.class)
            .should().resideInAPackage("..exception..")
            .orShould().resideInAPackage("..common.exceptions..")
            .because("Exception 클래스는 exception 패키지에 위치해야 합니다");
}
```

---

## 6. 적용 방법

### 6.1 Step 1: 의존성 추가

```groovy
// build.gradle
dependencies {
    // 기존 테스트 의존성
    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    // ArchUnit 추가
    testImplementation 'com.tngtech.archunit:archunit-junit5:1.2.1'
}
```

### 6.2 Step 2: 테스트 디렉토리 생성

```bash
mkdir -p src/test/java/com/mzc/backend/lms/architecture
```

### 6.3 Step 3: 테스트 파일 작성

위 5.3 ~ 5.6 섹션의 테스트 클래스를 생성합니다.

### 6.4 Step 4: 테스트 실행

```bash
# 전체 테스트 실행
./gradlew test

# 아키텍처 테스트만 실행
./gradlew test --tests "com.mzc.backend.lms.architecture.*"
```

### 6.5 Step 5: CI/CD 파이프라인 연동

**GitHub Actions 예시:**
```yaml
# .github/workflows/ci.yml
name: CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Run Tests (including Architecture Tests)
        run: ./gradlew test

      - name: Upload Test Results
        uses: actions/upload-artifact@v4
        if: always()
        with:
          name: test-results
          path: build/reports/tests/
```

---

## 7. 기대 효과

### 7.1 정량적 효과

| 지표 | Before | After | 개선율 |
|------|--------|-------|--------|
| 아키텍처 위반 발견 시점 | 코드 리뷰 (PR 단계) | 컴파일/테스트 단계 | 조기 발견 |
| 위반 감지 정확도 | 휴먼 에러 발생 가능 | 100% 자동 감지 | 100% |
| 신규 개발자 온보딩 시간 | 규칙 문서 읽기 필요 | 테스트로 규칙 학습 | 감소 |

### 7.2 정성적 효과

1. **아키텍처 일관성 유지**: 시간이 지나도 헥사고날 아키텍처 원칙 유지
2. **코드 리뷰 효율화**: 아키텍처 규칙은 자동 검증, 비즈니스 로직에 집중
3. **문서화**: 테스트 자체가 "실행 가능한 아키텍처 문서" 역할
4. **리팩토링 안전성**: 의존성 위반 시 즉시 피드백

### 7.3 운영 가이드

| 상황 | 대응 |
|------|------|
| 테스트 실패 시 | 위반 메시지 확인 → 코드 수정 → 재테스트 |
| 새 도메인 추가 시 | 기존 규칙 자동 적용됨 |
| 규칙 추가/변경 시 | 테스트 클래스 수정 → PR 리뷰 |
| 예외 허용 필요 시 | `@ArchIgnore` 또는 규칙 조건 수정 |

---

## 부록: 자주 발생하는 위반 사례

### 사례 1: Service에서 Controller DTO 직접 참조

**위반 코드:**
```java
// application/service/UserService.java
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.UserCreateRequest; // 위반!

public class UserService {
    public void createUser(UserCreateRequest request) { ... }
}
```

**해결:**
```java
// Service는 도메인 객체 또는 Command 객체 사용
public class UserService {
    public void createUser(CreateUserCommand command) { ... }
}

// Controller에서 변환
@PostMapping
public void create(@RequestBody UserCreateRequest request) {
    userService.createUser(request.toCommand());
}
```

### 사례 2: 도메인 간 직접 참조

**위반 코드:**
```java
// domains/enrollment/application/service/EnrollmentService.java
import com.mzc.backend.lms.domains.course.application.service.CourseService; // 위반!
```

**해결:**
```java
// Port 인터페이스를 통한 간접 참조
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort;
```

### 사례 3: Repository 구현체 직접 주입

**위반 코드:**
```java
// application/service/PostService.java
import com.mzc.backend.lms.domains.board.adapter.out.persistence.repository.JpaPostRepository; // 위반!

@Service
public class PostService {
    private final JpaPostRepository repository; // JPA Repository 직접 주입
}
```

**해결:**
```java
import com.mzc.backend.lms.domains.board.application.port.out.PostRepositoryPort;

@Service
public class PostService {
    private final PostRepositoryPort repository; // Port 인터페이스 주입
}
```

---

## 참고 자료

- [ArchUnit 공식 문서](https://www.archunit.org/userguide/html/000_Index.html)
- [ArchUnit GitHub](https://github.com/TNG/ArchUnit)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

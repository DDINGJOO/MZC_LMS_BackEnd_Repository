# Board Domain 초심자 가이드

이 가이드는 LMS 백엔드의 게시판 시스템을 이해하고 개발하기 위한 **핵심 내용**을 담고 있습니다.

## 📋 목차
1. [통합 게시글 테이블 설계](#1-통합-게시글-테이블-설계)
2. [9가지 게시판 유형](#2-9가지-게시판-유형)
3. [역할별 접근 권한](#3-역할별-접근-권한)
4. [관리자 전체 권한](#4-관리자-전체-권한)

---

## 1. 통합 게시글 테이블 설계

### 🎯 핵심 개념
**모든 게시판의 게시글을 하나의 테이블에서 관리**합니다. 게시판별로 테이블을 나누지 않고 `posts` 테이블 하나로 통합 관리합니다.

### 왜 통합 테이블을 사용하나요?
✅ **장점**
- 게시글 CRUD 로직을 한 번만 구현
- 전체 게시글 통합 검색 가능  
- 새 게시판 추가 시 테이블 생성 불필요
- 사용자별 전체 게시글 통계 쉽게 조회

⚠️ **단점**
- 테이블 크기가 커질 수 있음
- 일부 필드가 NULL일 수 있음

### 핵심 테이블 구조

```sql
Table posts {
  -- 기본 정보
  id bigint [pk, increment]
  category_id bigint [not null]      -- 어떤 게시판인지 (공지/자유/질문 등)
  course_id bigint                   -- 강의 관련 게시판에서만 사용
  author_id bigint [not null]        -- 작성자
  
  -- 콘텐츠
  title varchar(255) [not null]
  content text [not null]
  
  -- 속성
  post_type varchar(30) [not null]   -- 게시글 세부 유형
  is_anonymous boolean [default: false]
  is_pinned boolean [default: false] -- 상단 고정
  is_notice boolean [default: false] -- 공지사항 여부
  
  -- 통계
  view_count int [default: 0]
  like_count int [default: 0]
  comment_count int [default: 0]
  
  -- 시간 관리
  created_at timestamp [not null]
  updated_at timestamp
  deleted_at timestamp               -- 소프트 삭제
}
```

### 실제 사용 예시

```sql
-- 자유게시판 게시글 작성
INSERT INTO posts (category_id, author_id, title, content, post_type)
VALUES (2, 200, '맛집 추천합니다!', '캠퍼스 근처 맛집 소개...', 'GENERAL');

-- 질문게시판 게시글 작성 (특정 강의)
INSERT INTO posts (category_id, course_id, author_id, title, content, post_type, is_anonymous)
VALUES (4, 1001, 400, 'SQL JOIN 질문', 'INNER JOIN과 LEFT JOIN 차이점...', 'QUESTION', true);

-- 게시판별 목록 조회
SELECT p.*, u.name as author_name 
FROM posts p
JOIN users u ON p.author_id = u.id
JOIN board_categories bc ON p.category_id = bc.id
WHERE bc.board_type = 'FREE' AND p.deleted_at IS NULL
ORDER BY p.is_pinned DESC, p.created_at DESC;
```

---

## 2. 9가지 게시판 유형

### 📌 기본 게시판 (5개)
1. **학교 공지사항** - 학교 전체 공지
2. **자유 게시판** - 자유로운 소통 공간
3. **질문 게시판** - 강의별 질문/답변
4. **토론 게시판** - 주제별 토론
5. **학과 게시판** - 학과별 공지 및 소통

### 🎯 역할별 제한 게시판 (2개)
6. **교수 게시판** - 교수/TA만 이용 가능
7. **학생 게시판** - 학생만 이용 가능

### 💼 특수 목적 게시판 (2개)
8. **공모전 게시판** - 공모전 정보 및 팀 모집
9. **취업 게시판** - 채용 정보 및 면접 후기

### 게시판별 특징

| 게시판 | 주요 용도 | 익명 가능 | 첨부파일 | 해시태그 |
|--------|-----------|-----------|----------|----------|
| 학교공지 | 중요 공지사항 | ❌ | ✅ | ❌ |
| 자유게시판 | 자유 소통 | ✅ | ✅ | ✅ |
| 질문게시판 | 학습 질문 | ✅ | ✅ | ✅ |
| 토론게시판 | 주제 토론 | ❌ | ✅ | ✅ |
| 학과게시판 | 학과 공지/소통 | ❌ | ✅ | ❌ |
| 교수게시판 | 교수진 소통 | ❌ | ✅ | ❌ |
| 학생게시판 | 학생 전용 소통 | ✅ | ✅ | ✅ |
| 공모전게시판 | 공모전 정보 | ❌ | ✅ | ✅ |
| 취업게시판 | 취업 정보 | ✅ | ✅ | ✅ |

---

## 3. 역할별 접근 권한

### 👥 사용자 역할
- **STUDENT**: 학생
- **PROFESSOR**: 교수  
- **TEACHING_ASSISTANT**: 조교
- **ADMIN**: 관리자

### 🔐 접근 권한 매트릭스

| 게시판 | 학생 | 교수 | TA | 관리자 |
|--------|------|------|-----|--------|
| 학교 공지 | 읽기 | 읽기/쓰기 | 읽기 | 전체 |
| 자유 게시판 | 읽기/쓰기 | 읽기/쓰기 | 읽기/쓰기 | 전체 |
| 질문 게시판 | 읽기/쓰기 | 읽기/쓰기 | 읽기/쓰기 | 전체 |
| 토론 게시판 | 읽기/쓰기 | 읽기/쓰기 | 읽기/쓰기 | 전체 |
| 학과 게시판 | 읽기/댓글* | 읽기/쓰기* | 읽기/댓글* | 전체 |
| **교수 게시판** | ❌ 접근불가 | ✅ 전체 | ✅ 전체 | 전체 |
| **학생 게시판** | ✅ 전체 | ❌ 접근불가 | ❌ 접근불가 | 전체 |
| 공모전 게시판 | 읽기/쓰기 | 읽기/쓰기 | 읽기/쓰기 | 전체 |
| 취업 게시판 | 읽기/쓰기 | 읽기 | 읽기 | 전체 |

*해당 학과 소속자만

### 코드로 권한 확인

```java
// 교수게시판 접근 제한
@PreAuthorize("hasRole('PROFESSOR') or hasRole('TEACHING_ASSISTANT') or hasRole('ADMIN')")
public List<Post> getProfessorPosts() { 
    return postRepository.findByBoardType(BoardType.PROFESSOR);
}

// 학생게시판 접근 제한  
@PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
public List<Post> getStudentPosts() {
    return postRepository.findByBoardType(BoardType.STUDENT);
}

// 서비스 레벨에서 권한 체크
public List<Post> getAccessiblePosts(BoardType boardType, User user) {
    // 관리자는 모든 게시판 접근 가능
    if (user.getRole() == UserRole.ADMIN) {
        return postRepository.findByBoardType(boardType);
    }
    
    // 교수게시판 - 교수/TA만
    if (boardType == BoardType.PROFESSOR) {
        if (user.getRole() == UserRole.PROFESSOR || user.getRole() == UserRole.TEACHING_ASSISTANT) {
            return postRepository.findByBoardType(boardType);
        }
        throw new UnauthorizedException("교수게시판에 접근할 권한이 없습니다.");
    }
    
    // 학생게시판 - 학생만
    if (boardType == BoardType.STUDENT) {
        if (user.getRole() == UserRole.STUDENT) {
            return postRepository.findByBoardType(boardType);
        }
        throw new UnauthorizedException("학생게시판에 접근할 권한이 없습니다.");
    }
    
    // 나머지 게시판은 모든 사용자 접근 가능
    return postRepository.findByBoardType(boardType);
}
```

---

## 4. 관리자 전체 권한

### 🔑 관리자(ADMIN)의 특별 권한

관리자는 **모든 게시판에 제한 없이 접근**하고 **모든 기능을 사용**할 수 있습니다.

#### 📝 게시글 관리
- ✅ 모든 게시판의 모든 게시글 조회 (삭제된 것 포함)
- ✅ 모든 게시판에 게시글 작성
- ✅ 다른 사용자의 게시글 수정/삭제
- ✅ 삭제된 게시글 복구 (Soft Delete → 복구)
- ✅ 완전 삭제 (Hard Delete)
- ✅ 게시글 상단 고정/해제
- ✅ 일반 게시글을 공지사항으로 전환

#### 💬 댓글 관리
- ✅ 모든 댓글 조회/수정/삭제
- ✅ 삭제된 댓글 복구

#### 👥 사용자 관리  
- ✅ 특정 사용자 게시글/댓글 작성 금지
- ✅ 사용자 게시판 이용 일시 정지
- ✅ 사용자 활동 로그 조회

#### 🏷️ 시스템 관리
- ✅ 게시판 카테고리 생성/수정/삭제
- ✅ 해시태그 관리 (생성/수정/삭제/병합)
- ✅ 게시판별/사용자별 통계 조회
- ✅ 신고된 게시글 처리

### 관리자 전용 기능 예시

```java
@PreAuthorize("hasRole('ADMIN')")
@RestController
public class AdminBoardController {
    
    // 모든 게시글 조회 (삭제된 것 포함)
    @GetMapping("/admin/posts/all")
    public List<Post> getAllPosts(@RequestParam(defaultValue = "false") boolean includeDeleted) {
        if (includeDeleted) {
            return postRepository.findAllWithDeleted();
        }
        return postRepository.findAll();
    }
    
    // 게시글 복구
    @PostMapping("/admin/posts/{id}/restore") 
    public void restorePost(@PathVariable Long id) {
        postService.restoreDeletedPost(id);
    }
    
    // 하드 삭제
    @DeleteMapping("/admin/posts/{id}/permanent")
    public void permanentDelete(@PathVariable Long id) {
        postService.permanentDeletePost(id);
    }
    
    // 사용자 게시 금지
    @PostMapping("/admin/users/{id}/ban")
    public void banUser(@PathVariable Long id, @RequestParam int days) {
        userService.banUserFromPosting(id, days);
    }
}
```

### 📊 관리자 대시보드 메뉴

```
🏠 관리자 대시보드
├── 📈 통계 및 분석
│   ├── 게시판별 통계
│   ├── 사용자별 활동 통계  
│   └── 시간대별 활동 현황
├── 📝 게시글 관리
│   ├── 전체 게시글 조회
│   ├── 삭제된 게시글 관리
│   ├── 신고된 게시글 처리
│   └── 공지사항 관리
├── 👥 사용자 관리
│   ├── 게시 제한 사용자
│   ├── 활동 로그 조회
│   └── 권한 관리
├── 🏷️ 시스템 관리
│   ├── 카테고리 관리
│   ├── 해시태그 관리
│   └── 시스템 설정
└── 🔒 보안 관리
    ├── 관리자 활동 로그
    ├── 데이터 백업/복구
    └── 시스템 모니터링
```

---

## 🚀 개발 시 중요 포인트

### 1. 권한 체크는 항상 백엔드에서
- 프론트엔드의 메뉴 숨김은 UX용
- 실제 권한 검증은 컨트롤러/서비스에서 필수

### 2. 관리자는 모든 제약 없음
- 관리자 권한 체크 시 다른 조건 무시
- 모든 게시판, 모든 기능 접근 가능

### 3. Soft Delete 정책
- 실제 삭제 대신 `deleted_at` 필드 사용
- 관리자만 완전 삭제(Hard Delete) 가능

### 4. 익명 게시 처리
- `is_anonymous = true`시 작성자 정보 숨김
- 관리자는 익명 작성자도 확인 가능

이 가이드를 바탕으로 LMS 게시판 시스템을 개발하면 **확장 가능하고 유지보수가 쉬운 시스템**을 구축할 수 있습니다!
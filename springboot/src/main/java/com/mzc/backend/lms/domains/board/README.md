# Board Domain - 전체 테이블 설계

## 요구사항 분석
1. **학교 공지사항**: CRUD (댓글 없음)
2. **자유 게시판**: CRUD + 이미지 (본문, 하단)
3. **질문 게시판**: 학습 관련 질문/답변
4. **토론 게시판**: 주제별 토론 및 의견 교환
5. **교수 게시판**: 교수만 이용 가능한 전용 커뮤니티
6. **학생 게시판**: 학생만 이용 가능한 전용 커뮤니티
7. **학과 게시판**: 학과별 공지 및 관리
8. **공모전 게시판**: 공모전 정보 공유
9. **취업 게시판**: 취업 정보 및 후기 공유
10. **해시태그** 시스템

## 통합 테이블 설계

```dbml
// ===== 사용자 관련 테이블 (참조) =====

Table users {
  id bigint [pk, increment, note: "사용자 고유 ID"]
  login_id varchar(50) [unique, not null, note: "학번/교번"]
  password varchar(255) [not null, note: "암호화된 비밀번호"]
  email varchar(100) [unique, not null, note: "이메일"]
  name varchar(50) [not null, note: "이름"]
  role varchar(30) [not null, note: "역할 (STUDENT/PROFESSOR/TA/ADMIN)"]
  status varchar(20) [not null, default: 'ACTIVE', note: "계정 상태"]
  created_at timestamp [not null, default: `now()`, note: "생성일시"]
  updated_at timestamp [note: "수정일시"]
  deleted_at timestamp [note: "삭제일시 (Soft Delete)"]

  indexes {
    login_id
    email
    role
  }
}

// ===== 게시판 공통 테이블 =====

// 게시판 카테고리 (게시판 유형 관리)
Table board_categories {
  id bigint [pk, increment, note: "카테고리 고유 ID"]
  name varchar(50) [unique, not null, note: "카테고리 이름"]
  description varchar(255) [note: "카테고리 설명"]
  board_type varchar(30) [not null, note: "게시판 유형 (NOTICE/FREE/QUESTION/DISCUSSION/PROFESSOR/STUDENT/DEPARTMENT/CONTEST/CAREER)"]
  allow_comments boolean [default: true, note: "댓글 허용 여부"]
  allow_attachments boolean [default: true, note: "첨부파일 허용 여부"]
  allow_anonymous boolean [default: false, note: "익명 작성 허용 여부"]
  display_order int [default: 0, note: "표시 순서"]
  is_active boolean [default: true, note: "활성화 여부"]
  created_at timestamp [not null, default: `now()`, note: "생성일시"]
  updated_at timestamp [note: "수정일시"]
  deleted_at timestamp [note: "삭제일시 (Soft Delete)"]

  indexes {
    board_type
    display_order
    is_active
  }
}

// 통합 게시글 테이블
Table posts {
  id bigint [pk, increment, note: "게시글 고유 ID"]
  category_id bigint [not null, ref: > board_categories.id, note: "카테고리 ID"]
  course_id bigint [note: "강의 ID (질문 게시판용, 선택사항)"]
  department_id bigint [note: "학과 ID (학과 게시판용, 선택사항)"]
  author_id bigint [not null, ref: > users.id, note: "작성자 ID"]
  title varchar(255) [not null, note: "제목"]
  content text [not null, note: "내용"]
  post_type varchar(30) [not null, note: "게시글 유형 (NOTICE/GENERAL/QUESTION/DISCUSSION/PROFESSOR/STUDENT/DEPARTMENT/CONTEST/CAREER)"]
  status varchar(20) [not null, default: 'ACTIVE', note: "상태 (ACTIVE/CLOSED/ARCHIVED)"]
  priority varchar(20) [default: 'NORMAL', note: "우선순위 (HIGH/NORMAL/LOW)"]
  is_anonymous boolean [default: false, note: "익명 게시글 여부"]
  is_pinned boolean [default: false, note: "상단 고정 여부"]
  is_notice boolean [default: false, note: "공지사항 여부"]
  view_count int [default: 0, note: "조회수"]
  like_count int [default: 0, note: "좋아요 수"]
  comment_count int [default: 0, note: "댓글 수"]
  created_at timestamp [not null, default: `now()`, note: "생성일시"]
  updated_at timestamp [note: "수정일시"]
  deleted_at timestamp [note: "삭제일시 (Soft Delete)"]

  indexes {
    (category_id, created_at) [name: 'idx_category_created']
    (course_id, created_at) [name: 'idx_course_created']
    (department_id, created_at) [name: 'idx_department_created']
    (author_id, created_at) [name: 'idx_author_created']
    post_type
    status
    is_pinned
    is_notice
  }
}

// 댓글 테이블
Table comments {
  id bigint [pk, increment, note: "댓글 고유 ID"]
  post_id bigint [not null, ref: > posts.id, note: "게시글 ID"]
  author_id bigint [not null, ref: > users.id, note: "작성자 ID"]
  parent_comment_id bigint [ref: > comments.id, note: "부모 댓글 ID (대댓글용)"]
  content text [not null, note: "댓글 내용"]
  depth int [default: 0, note: "댓글 깊이 (0: 댓글, 1: 대댓글)"]
  is_anonymous boolean [default: false, note: "익명 댓글 여부"]
  like_count int [default: 0, note: "좋아요 수"]
  created_at timestamp [not null, default: `now()`, note: "생성일시"]
  updated_at timestamp [note: "수정일시"]
  deleted_at timestamp [note: "삭제일시 (Soft Delete)"]

  indexes {
    (post_id, created_at) [name: 'idx_post_created']
    (parent_comment_id, created_at) [name: 'idx_parent_created']
    (author_id, created_at) [name: 'idx_author_created']
    depth
  }
}



// ===== 첨부파일 테이블 =====

Table attachments {
  id bigint [pk, increment, note: "첨부파일 고유 ID"]
  post_id bigint [ref: > posts.id, note: "게시글 ID"]
  comment_id bigint [ref: > comments.id, note: "댓글 ID"]
  attachment_type varchar(30) [not null, note: "첨부 유형 (POST_CONTENT/POST_BOTTOM/COMMENT)"]
  original_name varchar(255) [not null, note: "원본 파일명"]
  stored_name varchar(255) [not null, note: "서버 저장 파일명 (UUID)"]
  file_path varchar(500) [not null, note: "파일 저장 경로"]
  file_size bigint [not null, note: "파일 크기 (bytes)"]
  mime_type varchar(100) [not null, note: "MIME 타입"]
  file_extension varchar(10) [note: "파일 확장자"]
  is_image boolean [default: false, note: "이미지 파일 여부"]
  download_count int [default: 0, note: "다운로드 횟수"]
  uploader_id bigint [not null, ref: > users.id, note: "업로더 ID"]
  created_at timestamp [not null, default: `now()`, note: "업로드일시"]
  updated_at timestamp [note: "수정일시"]
  deleted_at timestamp [note: "삭제일시 (Soft Delete)"]

  indexes {
    post_id
    comment_id
    uploader_id
    attachment_type
    mime_type
  }
}

// ===== 태그 시스템 =====

Table hashtags {
  id bigint [pk, increment, note: "해시태그 고유 ID"]
  name varchar(50) [unique, not null, note: "태그 이름 (#제외, 소문자)"]
  display_name varchar(50) [not null, note: "화면 표시용 태그명"]
  description varchar(255) [note: "태그 설명"]
  color varchar(7) [default: '#007bff', note: "태그 색상 (HEX)"]
  tag_category varchar(30) [note: "태그 카테고리 (SUBJECT/DIFFICULTY/TYPE 등)"]
  usage_count int [default: 0, note: "사용 횟수"]
  is_active boolean [default: true, note: "활성화 상태"]
  created_by bigint [ref: > users.id, note: "태그 생성자 ID"]
  created_at timestamp [not null, default: `now()`, note: "생성일시"]
  updated_at timestamp [note: "수정일시"]
  deleted_at timestamp [note: "삭제일시 (Soft Delete)"]

  indexes {
    name
    tag_category
    usage_count
    is_active
  }
}

Table post_hashtags {
  id bigint [pk, increment, note: "게시글-해시태그 연결 ID"]
  post_id bigint [not null, ref: > posts.id, note: "게시글 ID"]
  hashtag_id bigint [not null, ref: > hashtags.id, note: "해시태그 ID"]
  created_by bigint [not null, ref: > users.id, note: "태그 추가자 ID"]
  created_at timestamp [not null, default: `now()`, note: "연결 생성일시"]

  indexes {
    (post_id, hashtag_id) [unique, name: 'idx_post_hashtag']
    hashtag_id
    created_by
  }
}

// ===== 좋아요/북마크 시스템 =====

Table post_likes {
  id bigint [pk, increment, note: "좋아요 고유 ID"]
  user_id bigint [not null, ref: > users.id, note: "사용자 ID"]
  post_id bigint [ref: > posts.id, note: "게시글 ID"]
  comment_id bigint [ref: > comments.id, note: "댓글 ID"]
  like_type varchar(20) [not null, note: "좋아요 유형 (POST/COMMENT)"]
  created_at timestamp [not null, default: `now()`, note: "좋아요 생성일시"]
  deleted_at timestamp [note: "좋아요 취소일시 (Soft Delete)"]

  indexes {
    (user_id, post_id) [unique, name: 'idx_user_post_like']
    (user_id, comment_id) [unique, name: 'idx_user_comment_like']
    like_type
  }
}

Table post_bookmarks {
  id bigint [pk, increment, note: "북마크 고유 ID"]
  user_id bigint [not null, ref: > users.id, note: "사용자 ID"]
  post_id bigint [not null, ref: > posts.id, note: "게시글 ID"]
  bookmark_category varchar(50) [note: "북마크 카테고리"]
  notes text [note: "개인 메모"]
  created_at timestamp [not null, default: `now()`, note: "북마크 생성일시"]
  updated_at timestamp [note: "수정일시"]
  deleted_at timestamp [note: "북마크 삭제일시 (Soft Delete)"]

  indexes {
    (user_id, post_id) [unique, name: 'idx_user_post_bookmark']
    (user_id, bookmark_category) [name: 'idx_user_category']
  }
}
```

## 요구사항별 구현 방식

### 1. 교수 페이지 - 과제 등록
- `assignments` 테이블에 과제 정보 저장
- `posts` 테이블에 과제 게시글 저장
- 과제 유형, 점수, 제출 형식, 마감일 등 설정

### 2. 교수 페이지 - 점수 등록  
- `assignment_grades` 테이블에 점수 정보 저장
- 점수, 피드백, 채점 상태 관리

### 3. 학생 페이지 - 과제 제출
- `assignment_submissions` 테이블에 제출 정보 저장
- 텍스트 제출 + 첨부파일 지원

### 4. 학생 페이지 - 점수 확인
- `assignment_grades` 테이블에서 본인 점수 조회
- 점수 공개 상태 확인

### 5. 학교 공지사항 (댓글 없음)
- `board_categories`에서 `allow_comments = false`
- `posts` 테이블에 `is_notice = true`

### 6. 자유 게시판 (이미지 포함)
- `posts` 테이블 + `attachments` 테이블
- `attachment_type`으로 본문/하단 이미지 구분

### 7. 과제 게시판 (대댓글)
- `comments` 테이블의 `depth` 필드로 대댓글 구현
- `parent_comment_id`로 계층 구조 관리

### 8. 해시태그
- `hashtags` + `post_hashtags` 테이블
- 자동 완성, 인기 태그 기능 지원

// ===== Relationships Summary =====
// users 1--* posts (작성자)
// users 1--* comments (댓글 작성자)
// users 1--* attachments (업로더)
// users 1--* post_likes (좋아요 누른 사용자)
// users 1--* post_bookmarks (북마크한 사용자)
// users 1--* hashtags (해시태그 생성자)
// board_categories 1--* posts (카테고리-게시글)
// posts 1--* comments (게시글-댓글)
// posts 1--* attachments (게시글-첨부파일)
// posts 1--* post_hashtags (게시글-해시태그)
// comments 1--* attachments (댓글-첨부파일)
// hashtags 1--* post_hashtags (해시태그-게시글)

## Enums

```java
// 사용자 역할 (User 도메인에서 참조)
public enum UserRole {
    STUDENT,            // 학생
    PROFESSOR,          // 교수
    TEACHING_ASSISTANT, // 조교 (TA)
    ADMIN              // 관리자
}

// 사용자 상태 (User 도메인에서 참조)
public enum UserStatus {
    ACTIVE,    // 활성
    INACTIVE,  // 비활성
    SUSPENDED, // 정지
    WITHDRAWN, // 탈퇴
    PENDING    // 승인대기
}

// 게시판 유형
public enum BoardType {
    NOTICE,     // 공지사항
    FREE,       // 자유게시판  
    QUESTION,   // 질문게시판
    DISCUSSION, // 토론게시판
    PROFESSOR,  // 교수게시판
    STUDENT,    // 학생게시판
    DEPARTMENT, // 학과게시판
    CONTEST,    // 공모전게시판
    CAREER      // 취업게시판
}

// 게시글 유형
public enum PostType {
    NOTICE,     // 공지사항
    GENERAL,    // 일반 게시글
    QUESTION,   // 질문
    DISCUSSION, // 토론
    PROFESSOR,  // 교수 게시글
    STUDENT,    // 학생 게시글
    DEPARTMENT, // 학과 게시글
    CONTEST,    // 공모전 정보
    CAREER      // 취업 정보
}

// 게시글 상태
public enum PostStatus {
    ACTIVE,   // 활성
    CLOSED,   // 마감/종료
    ARCHIVED  // 아카이브
}

// 우선순위
public enum Priority {
    HIGH,    // 높음
    NORMAL,  // 보통
    LOW      // 낮음
}

// 첨부파일 유형
public enum AttachmentType {
    POST_CONTENT, // 본문 이미지
    POST_BOTTOM,  // 하단 첨부파일
    COMMENT       // 댓글 첨부파일
}

// 좋아요 유형
public enum LikeType {
    POST,    // 게시글 좋아요
    COMMENT  // 댓글 좋아요
}

// 태그 카테고리
public enum TagCategory {
    SUBJECT,    // 과목별
    DIFFICULTY, // 난이도별
    TYPE,       // 유형별
    GENERAL     // 일반
}
```

// ===== Notes =====
// - User 도메인 테이블 구조와 일관성 유지
// - 모든 테이블에 created_at, updated_at, deleted_at 표준 적용
// - Soft Delete 정책 전체 적용
// - 인덱스 최적화로 조회 성능 향상
// - 외래키 제약조건으로 데이터 무결성 보장
// - 통합 게시글 테이블로 확장성 및 유지보수성 향상
// - 역할 기반 접근 제어 (RBAC) 적용
// - 관리자(ADMIN)는 모든 게시판에 대한 전체 권한 보유
// - 교수/학생 전용 게시판으로 역할별 커뮤니티 분리
// - 학과게시판은 공식 공지 및 관리 용도로 활용

## 구현될 기능

### 1. 학교 공지사항
- 관리자/교수만 작성 가능
- 댓글 기능 비활성화
- 중요도별 상단 고정

### 2. 자유 게시판  
- 모든 사용자 작성 가능
- 이미지 첨부 (본문 + 하단)
- 댓글/대댓글 기능
- 해시태그 시스템

### 3. 질문 게시판
- 학습 관련 질문/답변
- 강의별 카테고리 (선택적)
- 댓글/대댓글 기능
- 좋아요/북마크 기능

### 4. 토론 게시판
- 주제별 토론 공간
- 댓글/대댓글 기능  
- 해시태그로 주제 분류

### 5. 교수 게시판
- 교수만 이용 가능한 전용 커뮤니티
- 교육 및 연구 관련 정보 공유
- 학사 운영 관련 논의

### 6. 학생 게시판
- 학생만 이용 가능한 전용 커뮤니티
- 학습 및 생활 정보 공유
- 동아리, 모임 등 학생 활동

### 7. 학과 게시판
- 학과별 공지사항 및 관리
- 학과 교수진의 공식 공지
- 학과 행사 및 중요 안내사항

### 8. 공모전 게시판
- 공모전 정보 및 팀 모집
- 마감일 정보 관리
- 분야별 해시태그 분류

### 9. 취업 게시판
- 채용 정보 공유
- 취업 후기 및 면접 정보
- 기업별/직무별 해시태그

### 10. 공통 기능
- 해시태그 시스템
- 좋아요/북마크
- 첨부파일 (이미지 + 문서)
- 익명 게시 (설정에 따라)
- 검색 및 필터링

이 테이블 설계로 모든 요구사항을 만족할 수 있습니다. 추가로 구현하고 싶은 기능이나 수정이 필요한 부분이 있다면 말씀해 주세요!
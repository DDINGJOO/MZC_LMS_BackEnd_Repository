-- =====================================================
-- 학생 20250101012 종합 더미 데이터 설정
-- 3학년 학생으로 변경, 과거 성적, 현재 수강, 과제/시험 등 추가
-- =====================================================

SET @STUDENT_ID = 20250101012;
SET @PROF_1 = 20250101002;
SET @PROF_2 = 20250101008;
SET @PROF_3 = 20250101009;
SET @PROF_4 = 20250101011;

-- =====================================================
-- 1. 과거 학기 추가 (2022, 2023, 2025 FALL)
-- =====================================================
INSERT IGNORE INTO academic_terms (year, term_type, start_date, end_date) VALUES
(2022, 'SPRING', '2022-03-01', '2022-06-30'),
(2022, 'FALL', '2022-09-01', '2022-12-31'),
(2023, 'SPRING', '2023-03-01', '2023-06-30'),
(2023, 'FALL', '2023-09-01', '2023-12-31'),
(2025, 'FALL', '2025-09-01', '2025-12-31');

-- 학기 ID 조회
SET @TERM_2022_SPRING = (SELECT id FROM academic_terms WHERE year = 2022 AND term_type = 'SPRING');
SET @TERM_2022_FALL = (SELECT id FROM academic_terms WHERE year = 2022 AND term_type = 'FALL');
SET @TERM_2023_SPRING = (SELECT id FROM academic_terms WHERE year = 2023 AND term_type = 'SPRING');
SET @TERM_2023_FALL = (SELECT id FROM academic_terms WHERE year = 2023 AND term_type = 'FALL');
SET @TERM_2024_SPRING = (SELECT id FROM academic_terms WHERE year = 2024 AND term_type = 'SPRING');
SET @TERM_2024_FALL = (SELECT id FROM academic_terms WHERE year = 2024 AND term_type = 'FALL');
SET @TERM_2025_SPRING = (SELECT id FROM academic_terms WHERE year = 2025 AND term_type = 'SPRING');
SET @TERM_2025_FALL = (SELECT id FROM academic_terms WHERE year = 2025 AND term_type = 'FALL');

-- =====================================================
-- 2. 학생 정보 수정 (3학년, 2022년 입학)
-- =====================================================
UPDATE students
SET grade = 3, admission_year = 2022
WHERE student_id = @STUDENT_ID;

-- =====================================================
-- 3. 과거 학기용 강의 추가
-- =====================================================

-- 2022 SPRING (1학년 1학기) - 기초 과목
INSERT INTO courses (subject_id, section_number, academic_term_id, professor_id, max_students, current_students) VALUES
(1, '101', @TERM_2022_SPRING, @PROF_1, 40, 35),
(2, '101', @TERM_2022_SPRING, @PROF_2, 35, 30);

SET @COURSE_22S_1 = LAST_INSERT_ID();
SET @COURSE_22S_2 = LAST_INSERT_ID() + 1;
SELECT @COURSE_22S_1 := id FROM courses WHERE subject_id = 1 AND section_number = '101' AND academic_term_id = @TERM_2022_SPRING LIMIT 1;
SELECT @COURSE_22S_2 := id FROM courses WHERE subject_id = 2 AND section_number = '101' AND academic_term_id = @TERM_2022_SPRING LIMIT 1;

-- 2022 FALL (1학년 2학기)
INSERT INTO courses (subject_id, section_number, academic_term_id, professor_id, max_students, current_students) VALUES
(3, '101', @TERM_2022_FALL, @PROF_3, 35, 32),
(4, '101', @TERM_2022_FALL, @PROF_1, 30, 28);

SELECT @COURSE_22F_1 := id FROM courses WHERE subject_id = 3 AND section_number = '101' AND academic_term_id = @TERM_2022_FALL LIMIT 1;
SELECT @COURSE_22F_2 := id FROM courses WHERE subject_id = 4 AND section_number = '101' AND academic_term_id = @TERM_2022_FALL LIMIT 1;

-- 2023 SPRING (2학년 1학기)
INSERT INTO courses (subject_id, section_number, academic_term_id, professor_id, max_students, current_students) VALUES
(5, '101', @TERM_2023_SPRING, @PROF_2, 30, 25),
(6, '101', @TERM_2023_SPRING, @PROF_4, 30, 27);

SELECT @COURSE_23S_1 := id FROM courses WHERE subject_id = 5 AND section_number = '101' AND academic_term_id = @TERM_2023_SPRING LIMIT 1;
SELECT @COURSE_23S_2 := id FROM courses WHERE subject_id = 6 AND section_number = '101' AND academic_term_id = @TERM_2023_SPRING LIMIT 1;

-- 2023 FALL (2학년 2학기)
INSERT INTO courses (subject_id, section_number, academic_term_id, professor_id, max_students, current_students) VALUES
(7, '101', @TERM_2023_FALL, @PROF_1, 25, 22),
(8, '101', @TERM_2023_FALL, @PROF_3, 25, 20);

SELECT @COURSE_23F_1 := id FROM courses WHERE subject_id = 7 AND section_number = '101' AND academic_term_id = @TERM_2023_FALL LIMIT 1;
SELECT @COURSE_23F_2 := id FROM courses WHERE subject_id = 8 AND section_number = '101' AND academic_term_id = @TERM_2023_FALL LIMIT 1;

-- 2025 SPRING (3학년 1학기) - 현재 학기 강의 추가
INSERT INTO courses (subject_id, section_number, academic_term_id, professor_id, max_students, current_students) VALUES
(1, '301', @TERM_2025_SPRING, @PROF_1, 40, 30),
(5, '301', @TERM_2025_SPRING, @PROF_2, 35, 28),
(7, '301', @TERM_2025_SPRING, @PROF_4, 30, 25);

SELECT @COURSE_25S_1 := id FROM courses WHERE subject_id = 1 AND section_number = '301' AND academic_term_id = @TERM_2025_SPRING LIMIT 1;
SELECT @COURSE_25S_2 := id FROM courses WHERE subject_id = 5 AND section_number = '301' AND academic_term_id = @TERM_2025_SPRING LIMIT 1;
SELECT @COURSE_25S_3 := id FROM courses WHERE subject_id = 7 AND section_number = '301' AND academic_term_id = @TERM_2025_SPRING LIMIT 1;

-- =====================================================
-- 4. 과거 학기 수강 등록 (Enrollments)
-- =====================================================
INSERT IGNORE INTO enrollments (course_id, student_id, enrolled_at) VALUES
(@COURSE_22S_1, @STUDENT_ID, '2022-02-25 10:00:00'),
(@COURSE_22S_2, @STUDENT_ID, '2022-02-25 10:05:00'),
(@COURSE_22F_1, @STUDENT_ID, '2022-08-25 09:30:00'),
(@COURSE_22F_2, @STUDENT_ID, '2022-08-25 09:35:00'),
(@COURSE_23S_1, @STUDENT_ID, '2023-02-24 11:00:00'),
(@COURSE_23S_2, @STUDENT_ID, '2023-02-24 11:10:00'),
(@COURSE_23F_1, @STUDENT_ID, '2023-08-24 10:00:00'),
(@COURSE_23F_2, @STUDENT_ID, '2023-08-24 10:15:00'),
(10, @STUDENT_ID, '2024-02-26 10:00:00'),
(11, @STUDENT_ID, '2024-02-26 10:10:00'),
(12, @STUDENT_ID, '2024-08-26 09:00:00'),
(13, @STUDENT_ID, '2024-08-26 09:10:00'),
(@COURSE_25S_1, @STUDENT_ID, '2025-02-25 09:00:00'),
(@COURSE_25S_2, @STUDENT_ID, '2025-02-25 09:10:00'),
(@COURSE_25S_3, @STUDENT_ID, '2025-02-25 09:20:00');

-- =====================================================
-- 5. 과거 학기 성적 등록 (Grades) - 전체 학기
-- =====================================================

-- 2022 SPRING 성적
INSERT INTO grades (academic_term_id, course_id, student_id, midterm_score, final_exam_score, assignment_score, quiz_score, attendance_score, final_score, final_grade, status, graded_at, published_at, created_at, updated_at) VALUES
(@TERM_2022_SPRING, @COURSE_22S_1, @STUDENT_ID, 92.00, 95.00, 88.00, 90.00, 100.00, 93.20, 'A+', 'PUBLISHED', '2022-06-25 14:00:00', '2022-06-28 09:00:00', '2022-06-25 14:00:00', '2022-06-28 09:00:00'),
(@TERM_2022_SPRING, @COURSE_22S_2, @STUDENT_ID, 88.00, 91.00, 85.00, 87.00, 95.00, 89.00, 'A', 'PUBLISHED', '2022-06-25 15:00:00', '2022-06-28 09:00:00', '2022-06-25 15:00:00', '2022-06-28 09:00:00');

-- 2022 FALL 성적
INSERT INTO grades (academic_term_id, course_id, student_id, midterm_score, final_exam_score, assignment_score, quiz_score, attendance_score, final_score, final_grade, status, graded_at, published_at, created_at, updated_at) VALUES
(@TERM_2022_FALL, @COURSE_22F_1, @STUDENT_ID, 85.00, 88.00, 90.00, 82.00, 100.00, 87.20, 'A', 'PUBLISHED', '2022-12-22 14:00:00', '2022-12-26 09:00:00', '2022-12-22 14:00:00', '2022-12-26 09:00:00'),
(@TERM_2022_FALL, @COURSE_22F_2, @STUDENT_ID, 82.00, 85.00, 88.00, 80.00, 90.00, 84.50, 'B+', 'PUBLISHED', '2022-12-22 15:00:00', '2022-12-26 09:00:00', '2022-12-22 15:00:00', '2022-12-26 09:00:00');

-- 2023 SPRING 성적
INSERT INTO grades (academic_term_id, course_id, student_id, midterm_score, final_exam_score, assignment_score, quiz_score, attendance_score, final_score, final_grade, status, graded_at, published_at, created_at, updated_at) VALUES
(@TERM_2023_SPRING, @COURSE_23S_1, @STUDENT_ID, 94.00, 96.00, 92.00, 95.00, 100.00, 95.00, 'A+', 'PUBLISHED', '2023-06-23 14:00:00', '2023-06-27 09:00:00', '2023-06-23 14:00:00', '2023-06-27 09:00:00'),
(@TERM_2023_SPRING, @COURSE_23S_2, @STUDENT_ID, 89.00, 92.00, 87.00, 88.00, 95.00, 90.00, 'A', 'PUBLISHED', '2023-06-23 15:00:00', '2023-06-27 09:00:00', '2023-06-23 15:00:00', '2023-06-27 09:00:00');

-- 2023 FALL 성적
INSERT INTO grades (academic_term_id, course_id, student_id, midterm_score, final_exam_score, assignment_score, quiz_score, attendance_score, final_score, final_grade, status, graded_at, published_at, created_at, updated_at) VALUES
(@TERM_2023_FALL, @COURSE_23F_1, @STUDENT_ID, 90.00, 93.00, 91.00, 89.00, 100.00, 91.50, 'A', 'PUBLISHED', '2023-12-21 14:00:00', '2023-12-26 09:00:00', '2023-12-21 14:00:00', '2023-12-26 09:00:00'),
(@TERM_2023_FALL, @COURSE_23F_2, @STUDENT_ID, 91.00, 89.00, 93.00, 90.00, 95.00, 90.80, 'A', 'PUBLISHED', '2023-12-21 15:00:00', '2023-12-26 09:00:00', '2023-12-21 15:00:00', '2023-12-26 09:00:00');

-- 2024 SPRING 성적 (기존 course 10, 11)
INSERT INTO grades (academic_term_id, course_id, student_id, midterm_score, final_exam_score, assignment_score, quiz_score, attendance_score, final_score, final_grade, status, graded_at, published_at, created_at, updated_at) VALUES
(@TERM_2024_SPRING, 10, @STUDENT_ID, 88.00, 90.00, 85.00, 87.00, 100.00, 88.50, 'A', 'PUBLISHED', '2024-06-24 14:00:00', '2024-06-28 09:00:00', '2024-06-24 14:00:00', '2024-06-28 09:00:00'),
(@TERM_2024_SPRING, 11, @STUDENT_ID, 92.00, 94.00, 90.00, 91.00, 95.00, 92.20, 'A+', 'PUBLISHED', '2024-06-24 15:00:00', '2024-06-28 09:00:00', '2024-06-24 15:00:00', '2024-06-28 09:00:00');

-- 2024 FALL 성적 (기존 course 12, 13)
INSERT INTO grades (academic_term_id, course_id, student_id, midterm_score, final_exam_score, assignment_score, quiz_score, attendance_score, final_score, final_grade, status, graded_at, published_at, created_at, updated_at) VALUES
(@TERM_2024_FALL, 12, @STUDENT_ID, 86.00, 88.00, 90.00, 85.00, 100.00, 88.00, 'A', 'PUBLISHED', '2024-12-20 14:00:00', '2024-12-24 09:00:00', '2024-12-20 14:00:00', '2024-12-24 09:00:00'),
(@TERM_2024_FALL, 13, @STUDENT_ID, 90.00, 92.00, 88.00, 91.00, 95.00, 90.80, 'A', 'PUBLISHED', '2024-12-20 15:00:00', '2024-12-24 09:00:00', '2024-12-20 15:00:00', '2024-12-24 09:00:00');

-- 2025 SPRING 현재 학기 (PENDING)
INSERT INTO grades (academic_term_id, course_id, student_id, midterm_score, assignment_score, quiz_score, attendance_score, status, created_at) VALUES
(@TERM_2025_SPRING, @COURSE_25S_1, @STUDENT_ID, 85.00, 78.00, 82.00, 95.00, 'PENDING', NOW()),
(@TERM_2025_SPRING, @COURSE_25S_2, @STUDENT_ID, 90.00, 88.00, 85.00, 100.00, 'PENDING', NOW()),
(@TERM_2025_SPRING, @COURSE_25S_3, @STUDENT_ID, 88.00, 92.00, 90.00, 95.00, 'PENDING', NOW());

-- =====================================================
-- 6. 현재 강의 스케줄 추가
-- =====================================================
INSERT INTO course_schedules (course_id, day_of_week, start_time, end_time, schedule_room) VALUES
(@COURSE_25S_1, 'MONDAY', '09:00:00', '10:30:00', '공학관 201호'),
(@COURSE_25S_1, 'WEDNESDAY', '09:00:00', '10:30:00', '공학관 201호'),
(@COURSE_25S_2, 'TUESDAY', '13:00:00', '14:30:00', '정보관 301호'),
(@COURSE_25S_2, 'THURSDAY', '13:00:00', '14:30:00', '정보관 301호'),
(@COURSE_25S_3, 'MONDAY', '14:00:00', '15:30:00', '공학관 405호'),
(@COURSE_25S_3, 'FRIDAY', '10:00:00', '11:30:00', '공학관 405호');

-- =====================================================
-- 7. 강의 공지사항 추가 (course_notices)
-- =====================================================
INSERT INTO course_notices (course_id, title, content, allow_comments, created_by, created_at, updated_at) VALUES
(@COURSE_25S_1, '중간고사 안내', '중간고사는 4월 15일(월) 09:00~10:30에 공학관 201호에서 진행됩니다.\n\n시험 범위: 1주차~7주차\n준비물: 학생증, 필기구', 1, @PROF_1, '2025-04-01 10:00:00', '2025-04-01 10:00:00'),
(@COURSE_25S_1, '과제 제출 마감 연장', '1차 과제 제출 마감이 4월 5일로 연장되었습니다.\n\n늦은 제출은 감점 처리됩니다.', 1, @PROF_1, '2025-03-28 14:00:00', '2025-03-28 14:00:00'),
(@COURSE_25S_2, '팀 프로젝트 조 편성 안내', '팀 프로젝트 조 편성이 완료되었습니다.\n\n조별 인원: 4명\n발표일: 6월 첫째 주', 1, @PROF_2, '2025-03-15 09:00:00', '2025-03-15 09:00:00'),
(@COURSE_25S_2, '실습 환경 설정 안내', 'MySQL 8.0 설치가 필요합니다.\n\n설치 가이드는 학습자료실에 업로드되어 있습니다.', 1, @PROF_2, '2025-03-05 11:00:00', '2025-03-05 11:00:00'),
(@COURSE_25S_3, '기말 프로젝트 주제 발표', '기말 프로젝트 주제를 4월 20일까지 제출해주세요.\n\n팀당 1개 주제 제출\n주제 승인 후 개발 시작', 1, @PROF_4, '2025-04-10 15:00:00', '2025-04-10 15:00:00'),
(@COURSE_25S_3, '휴강 안내', '4월 25일(금)은 학회 참석으로 휴강입니다.\n\n보강은 추후 공지 예정입니다.', 1, @PROF_4, '2025-04-20 09:00:00', '2025-04-20 09:00:00');

-- =====================================================
-- 8. 게시판 카테고리 ID 확인
-- =====================================================
SET @ASSIGNMENT_CATEGORY = (SELECT id FROM board_categories WHERE board_type = 'ASSIGNMENT' LIMIT 1);
SET @EXAM_CATEGORY = (SELECT id FROM board_categories WHERE board_type = 'EXAM' LIMIT 1);

-- 카테고리가 없으면 기본값 사용 (1)
SET @ASSIGNMENT_CATEGORY = IFNULL(@ASSIGNMENT_CATEGORY, 1);
SET @EXAM_CATEGORY = IFNULL(@EXAM_CATEGORY, 1);

-- =====================================================
-- 9. 게시글 및 과제 추가
-- =====================================================
INSERT INTO posts (category_id, title, content, author_id, view_count, is_deleted, is_pinned, created_at, updated_at) VALUES
(@ASSIGNMENT_CATEGORY, '[컴퓨터 개론] 1차 과제: 프로그래밍 기초', '## 과제 안내\n\n아래 문제들을 풀고 제출해주세요.\n\n### 문제\n1. 변수와 자료형에 대해 설명하시오. (30점)\n2. 조건문을 활용한 프로그램을 작성하시오. (40점)\n3. 반복문을 활용한 프로그램을 작성하시오. (30점)\n\n### 제출 방법\n- 파일 형식: PDF 또는 ZIP\n- 파일명: 학번_이름_과제1.pdf\n\n**제출 기한: 2025-04-05 23:59**', @PROF_1, 45, false, false, '2025-03-20 10:00:00', '2025-03-20 10:00:00'),
(@ASSIGNMENT_CATEGORY, '[데이터베이스] DB 설계 과제', '## DB 설계 과제\n\nERD를 설계하고 SQL DDL을 작성하여 제출하세요.\n\n### 주제\n도서관 관리 시스템\n\n### 요구사항\n- 회원 관리 (회원번호, 이름, 연락처)\n- 도서 관리 (ISBN, 제목, 저자, 출판사)\n- 대출/반납 관리\n\n### 제출물\n1. ERD 다이어그램 (draw.io 또는 ERDCloud 사용)\n2. DDL 스크립트\n3. 샘플 데이터 INSERT 문\n\n**제출 기한: 2025-04-12 23:59**', @PROF_2, 38, false, false, '2025-03-25 14:00:00', '2025-03-25 14:00:00'),
(@ASSIGNMENT_CATEGORY, '[소프트웨어 공학] 요구사항 분석서 작성', '## 팀 프로젝트 - 요구사항 분석서\n\n### 포함 내용\n- 기능 요구사항 (최소 10개)\n- 비기능 요구사항 (최소 5개)\n- 유스케이스 다이어그램\n- 유스케이스 명세서\n\n### 제출 형식\n- Word 또는 PDF\n- 팀당 1부 제출\n\n**제출 기한: 2025-04-20 23:59**', @PROF_4, 32, false, false, '2025-04-01 09:00:00', '2025-04-01 09:00:00');

SET @POST_ASSIGN_1 = LAST_INSERT_ID();
SET @POST_ASSIGN_2 = @POST_ASSIGN_1 + 1;
SET @POST_ASSIGN_3 = @POST_ASSIGN_1 + 2;

-- 시험 게시글
INSERT INTO posts (category_id, title, content, author_id, view_count, is_deleted, is_pinned, created_at, updated_at) VALUES
(@EXAM_CATEGORY, '[컴퓨터 개론] 중간고사', '## 중간고사 안내\n\n**범위**: 1주차 ~ 7주차\n**장소**: 공학관 201호\n**시간**: 90분\n**문항수**: 25문항\n**배점**: 100점\n\n### 유의사항\n- 학생증 지참 필수\n- 계산기 사용 불가\n- 부정행위 적발 시 F 처리', @PROF_1, 52, false, true, '2025-04-01 10:00:00', '2025-04-01 10:00:00'),
(@EXAM_CATEGORY, '[데이터베이스] 퀴즈 1', '## SQL 기초 퀴즈\n\n**범위**: SQL DDL, DML 기초\n**시간**: 30분\n**문항수**: 10문항\n**배점**: 20점\n\n온라인으로 진행됩니다.\nLMS에서 응시해주세요.', @PROF_2, 40, false, false, '2025-03-28 11:00:00', '2025-03-28 11:00:00');

SET @POST_EXAM_1 = LAST_INSERT_ID();
SET @POST_EXAM_2 = @POST_EXAM_1 + 1;

-- 과제 테이블 추가
INSERT INTO assignments (post_id, course_id, due_date, max_score, submission_method, late_submission_allowed, late_penalty_percent, max_file_size_mb, allowed_file_types, instructions, created_by, created_at, updated_at) VALUES
(@POST_ASSIGN_1, @COURSE_25S_1, '2025-04-05 23:59:59', 100.00, 'FILE', 1, 0.10, 10, 'pdf,zip,docx', '파일명은 학번_이름_과제1 형식으로 제출하세요.', @PROF_1, '2025-03-20 10:00:00', '2025-03-20 10:00:00'),
(@POST_ASSIGN_2, @COURSE_25S_2, '2025-04-12 23:59:59', 100.00, 'FILE', 1, 0.15, 20, 'pdf,zip,sql', 'ERD와 SQL 파일을 ZIP으로 압축하여 제출하세요.', @PROF_2, '2025-03-25 14:00:00', '2025-03-25 14:00:00'),
(@POST_ASSIGN_3, @COURSE_25S_3, '2025-04-20 23:59:59', 100.00, 'FILE', 0, NULL, 15, 'pdf,docx', '팀당 1부만 제출하세요. 팀장이 대표로 제출합니다.', @PROF_4, '2025-04-01 09:00:00', '2025-04-01 09:00:00');

-- 시험 테이블 추가
INSERT INTO exams (post_id, course_id, exam_type, exam_date, duration_minutes, total_score, is_online, location, instructions, question_count, passing_score, created_by, created_at, updated_at) VALUES
(@POST_EXAM_1, @COURSE_25S_1, 'MIDTERM', '2025-04-15 09:00:00', 90, 100.00, 0, '공학관 201호', '학생증 지참 필수. 계산기 사용 불가.', 25, 60.00, @PROF_1, '2025-04-01 10:00:00', '2025-04-01 10:00:00'),
(@POST_EXAM_2, @COURSE_25S_2, 'QUIZ', '2025-04-02 13:00:00', 30, 20.00, 1, NULL, 'LMS에서 온라인으로 응시합니다.', 10, 12.00, @PROF_2, '2025-03-28 11:00:00', '2025-03-28 11:00:00');

-- =====================================================
-- 10. 주차별 콘텐츠 추가 (course_weeks)
-- =====================================================
INSERT INTO course_weeks (course_id, week_number, title, start_date, end_date, created_at) VALUES
(@COURSE_25S_1, 1, '프로그래밍 기초 개념', '2025-03-03', '2025-03-09', '2025-03-01 00:00:00'),
(@COURSE_25S_1, 2, '변수와 자료형', '2025-03-10', '2025-03-16', '2025-03-01 00:00:00'),
(@COURSE_25S_1, 3, '조건문과 반복문', '2025-03-17', '2025-03-23', '2025-03-01 00:00:00'),
(@COURSE_25S_1, 4, '함수의 이해', '2025-03-24', '2025-03-30', '2025-03-01 00:00:00'),
(@COURSE_25S_1, 5, '배열과 문자열', '2025-03-31', '2025-04-06', '2025-03-01 00:00:00'),
(@COURSE_25S_1, 6, '포인터 기초', '2025-04-07', '2025-04-13', '2025-03-01 00:00:00'),
(@COURSE_25S_1, 7, '구조체', '2025-04-14', '2025-04-20', '2025-03-01 00:00:00'),
(@COURSE_25S_2, 1, '데이터베이스 개요', '2025-03-03', '2025-03-09', '2025-03-01 00:00:00'),
(@COURSE_25S_2, 2, '관계형 데이터 모델', '2025-03-10', '2025-03-16', '2025-03-01 00:00:00'),
(@COURSE_25S_2, 3, 'SQL DDL', '2025-03-17', '2025-03-23', '2025-03-01 00:00:00'),
(@COURSE_25S_2, 4, 'SQL DML', '2025-03-24', '2025-03-30', '2025-03-01 00:00:00'),
(@COURSE_25S_2, 5, '정규화 이론', '2025-03-31', '2025-04-06', '2025-03-01 00:00:00'),
(@COURSE_25S_3, 1, '소프트웨어 공학 개요', '2025-03-03', '2025-03-09', '2025-03-01 00:00:00'),
(@COURSE_25S_3, 2, '요구사항 분석', '2025-03-10', '2025-03-16', '2025-03-01 00:00:00'),
(@COURSE_25S_3, 3, '설계 패턴', '2025-03-17', '2025-03-23', '2025-03-01 00:00:00'),
(@COURSE_25S_3, 4, 'UML 다이어그램', '2025-03-24', '2025-03-30', '2025-03-01 00:00:00');

-- 완료 확인
SELECT '===== 더미 데이터 삽입 완료 =====' as result;
SELECT '학생 정보:' as info;
SELECT student_id, grade, admission_year FROM students WHERE student_id = @STUDENT_ID;
SELECT '수강 현황:' as info;
SELECT COUNT(*) as total_enrollments FROM enrollments WHERE student_id = @STUDENT_ID;
SELECT '성적 현황:' as info;
SELECT COUNT(*) as total_grades,
       SUM(CASE WHEN status = 'PUBLISHED' THEN 1 ELSE 0 END) as published,
       SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) as pending
FROM grades WHERE student_id = @STUDENT_ID;
SELECT '과목별 성적:' as info;
SELECT g.academic_term_id, at.year, at.term_type, g.final_grade, g.final_score
FROM grades g
JOIN academic_terms at ON g.academic_term_id = at.id
WHERE g.student_id = @STUDENT_ID AND g.status = 'PUBLISHED'
ORDER BY at.year, at.term_type;

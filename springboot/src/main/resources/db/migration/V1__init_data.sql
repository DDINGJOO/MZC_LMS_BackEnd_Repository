-- =====================================================
-- LMS 초기 데이터 스크립트 (배포용)
-- V1__init_data.sql
-- 시스템 운영에 필수적인 기초 데이터만 포함
-- =====================================================

-- -----------------------------------------------------
-- 1. 사용자 유형 (user_types)
-- -----------------------------------------------------
INSERT INTO user_types (id, type_code, type_name)
VALUES (1, 'STUDENT', '학생'),
       (2, 'PROFESSOR', '교수');

-- -----------------------------------------------------
-- 2. 사용자 상태 유형 (user_status_types)
-- -----------------------------------------------------
INSERT INTO user_status_types (id, status_code, status_name)
VALUES (1, 'ACTIVE', '활성'),
       (2, 'INACTIVE', '비활성'),
       (3, 'SUSPENDED', '정지');

-- -----------------------------------------------------
-- 3. 단과대학 (colleges)
-- -----------------------------------------------------
INSERT INTO colleges (id, college_code, college_number_code, college_name)
VALUES (1, 'ENG', '01', '공과대학'),
       (2, 'BUS', '02', '경영대학'),
       (3, 'HUM', '03', '인문대학'),
       (4, 'NAT', '04', '자연과학대학'),
       (5, 'SOC', '05', '사회과학대학'),
       (6, 'ART', '06', '예술대학');

-- -----------------------------------------------------
-- 4. 학과 (departments)
-- -----------------------------------------------------
-- 공과대학 (college_id = 1)
INSERT INTO departments (id, college_id, department_code, department_name)
VALUES (1, 1, 'CS', '컴퓨터공학과'),
       (2, 1, 'EE', '전자공학과'),
       (3, 1, 'ME', '기계공학과'),
       (4, 1, 'CE', '화학공학과'),
       (5, 1, 'CV', '건설환경공학과');

-- 경영대학 (college_id = 2)
INSERT INTO departments (id, college_id, department_code, department_name)
VALUES (6, 2, 'BA', '경영학과'),
       (7, 2, 'ACC', '회계학과'),
       (8, 2, 'FIN', '금융학과'),
       (9, 2, 'MKT', '마케팅학과');

-- 인문대학 (college_id = 3)
INSERT INTO departments (id, college_id, department_code, department_name)
VALUES (10, 3, 'KOR', '국어국문학과'),
       (11, 3, 'ENG', '영어영문학과'),
       (12, 3, 'HIS', '사학과'),
       (13, 3, 'PHI', '철학과');

-- 자연과학대학 (college_id = 4)
INSERT INTO departments (id, college_id, department_code, department_name)
VALUES (14, 4, 'MATH', '수학과'),
       (15, 4, 'PHY', '물리학과'),
       (16, 4, 'CHEM', '화학과'),
       (17, 4, 'BIO', '생명과학과');

-- 사회과학대학 (college_id = 5)
INSERT INTO departments (id, college_id, department_code, department_name)
VALUES (18, 5, 'PSY', '심리학과'),
       (19, 5, 'SOC', '사회학과'),
       (20, 5, 'POL', '정치외교학과'),
       (21, 5, 'ECO', '경제학과');

-- 예술대학 (college_id = 6)
INSERT INTO departments (id, college_id, department_code, department_name)
VALUES (22, 6, 'MUS', '음악학과'),
       (23, 6, 'ART', '미술학과'),
       (24, 6, 'DES', '디자인학과');

-- -----------------------------------------------------
-- 5. 알림 타입 (notification_types)
-- -----------------------------------------------------
INSERT INTO notification_types (id, type_code, type_name, category, default_message_template, is_active)
VALUES
-- 시스템 알림
(1, 'SYSTEM_NOTICE', '시스템 공지', 'SYSTEM', '시스템 공지사항이 있습니다: {message}', true),
(2, 'SYSTEM_MAINTENANCE', '시스템 점검', 'SYSTEM', '시스템 점검 안내: {message}', true),

-- 계정 관련 알림
(3, 'ACCOUNT_CREATED', '계정 생성', 'ACCOUNT', '계정이 성공적으로 생성되었습니다.', true),
(4, 'PASSWORD_CHANGED', '비밀번호 변경', 'ACCOUNT', '비밀번호가 성공적으로 변경되었습니다.', true),
(5, 'PROFILE_UPDATED', '프로필 수정', 'ACCOUNT', '프로필 정보가 업데이트되었습니다.', true),

-- 학사 관련 알림
(6, 'COURSE_ENROLLED', '수강 신청', 'ACADEMIC', '{courseName} 과목이 수강 신청되었습니다.', true),
(7, 'COURSE_DROPPED', '수강 취소', 'ACADEMIC', '{courseName} 과목이 수강 취소되었습니다.', true),
(8, 'GRADE_POSTED', '성적 등록', 'ACADEMIC', '{courseName} 과목의 성적이 등록되었습니다.', true),
(9, 'ASSIGNMENT_DUE', '과제 마감', 'ACADEMIC', '{assignmentName} 과제 마감일이 다가옵니다.', true),
(10, 'ATTENDANCE_WARNING', '출석 경고', 'ACADEMIC', '{courseName} 과목의 출석률이 낮습니다.', true),

-- 댓글 관련 알림
(11, 'COMMENT_CREATED', '댓글 알림', '게시판', '게시글에 새 댓글이 달렸습니다.', true),
(12, 'REPLY_CREATED', '대댓글 알림', '게시판', '댓글에 답글이 달렸습니다.', true),

-- 강의 관련 알림
(13, 'DAILY_COURSE_REMINDER', '강의별 오늘 알림', '강의', '오늘의 강의 일정을 확인하세요.', true),
(14, 'COURSE_NOTICE_CREATED', '강의 공지사항', '강의', '새로운 강의 공지사항이 등록되었습니다.', true);

-- -----------------------------------------------------
-- 6. 기간 유형 (period_types)
-- -----------------------------------------------------
INSERT INTO period_types (id, type_code, type_name, description)
VALUES (1, 'ENROLLMENT', '수강신청', '학생이 수강신청을 할 수 있는 기간'),
       (2, 'COURSE_REGISTRATION', '강의등록', '교수가 강의를 등록할 수 있는 기간'),
       (3, 'ADJUSTMENT', '정정', '수강신청 정정 기간'),
       (4, 'CANCELLATION', '수강철회', '수강철회가 가능한 기간'),
       (5, 'GRADE_CALCULATION', '성적산출', '교수가 점수 산출(계산)을 수행할 수 있는 기간'),
       (6, 'GRADE_PUBLISH', '성적공개', '교수가 등급을 확정/공개할 수 있는 기간');

-- -----------------------------------------------------
-- 7. 과목 유형 (course_types)
-- -----------------------------------------------------
INSERT INTO course_types (id, type_code, category)
VALUES (1, 1, 0), -- 전공필수
       (2, 2, 0), -- 전공선택
       (3, 3, 1), -- 교양필수
       (4, 4, 1);
-- 교양선택

-- -----------------------------------------------------
-- 8. 게시판 카테고리 (board_categories)
-- -----------------------------------------------------
INSERT INTO board_categories (id, board_type, allow_comments, allow_attachments, allow_anonymous, is_deleted,
                              created_at, updated_at)
VALUES (1, 'NOTICE', true, true, false, false, NOW(), NOW()),
       (2, 'FREE', true, true, true, false, NOW(), NOW()),
       (3, 'QUESTION', true, true, true, false, NOW(), NOW()),
       (4, 'DISCUSSION', true, true, false, false, NOW(), NOW()),
       (5, 'DEPARTMENT', true, true, false, false, NOW(), NOW()),
       (6, 'PROFESSOR', true, true, false, false, NOW(), NOW()),
       (7, 'STUDENT', true, true, true, false, NOW(), NOW()),
       (8, 'CONTEST', true, true, false, false, NOW(), NOW()),
       (9, 'CAREER', true, true, true, false, NOW(), NOW()),
       (10, 'ASSIGNMENT', false, true, false, false, NOW(), NOW()),
       (11, 'EXAM', false, true, false, false, NOW(), NOW()),
       (12, 'QUIZ', false, false, false, false, NOW(), NOW()),
       (13, 'STUDY_RECRUITMENT', true, false, false, false, NOW(), NOW());

-- -----------------------------------------------------
-- 9. 학기 (academic_terms) - 예시 데이터
-- -----------------------------------------------------
INSERT INTO academic_terms (id, year, term_type, start_date, end_date)
VALUES (1, 2025, '2', '2025-09-01', '2025-12-31');

-- -----------------------------------------------------
-- 10. 수강신청/강의등록 기간 (enrollment_periods)
-- -----------------------------------------------------
INSERT INTO enrollment_periods (id, term_id, period_name, period_type_id, start_datetime, end_datetime, target_year,
                                created_at)
VALUES (1, 1, '1차 수강신청', 1, '2025-09-01 00:00:00', '2025-12-31 23:59:59', 0, NOW()),
       (2, 1, '강의등록', 2, '2025-08-01 00:00:00', '2025-08-31 23:59:59', 0, NOW()),
       (3, 1, '성적산출기간', 5, '2025-12-01 00:00:00', '2025-12-31 23:59:59', 0, NOW()),
       (4, 1, '성적공개기간', 6, '2026-01-01 00:00:00', '2026-01-15 23:59:59', 0, NOW());

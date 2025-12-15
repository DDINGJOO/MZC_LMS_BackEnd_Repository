package com.mzc.backend.lms.domains.user.auth.encryption.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 암호화된 필드를 표시하는 어노테이션
 * 응답 시 자동으로 복호화됨
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypted {
}

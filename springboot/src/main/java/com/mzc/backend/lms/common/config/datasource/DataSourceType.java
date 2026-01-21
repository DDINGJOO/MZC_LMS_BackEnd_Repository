package com.mzc.backend.lms.common.config.datasource;

/**
 * 데이터소스 타입 정의
 * - PRIMARY: 쓰기 전용 Master DB
 * - REPLICA: 읽기 전용 Slave DB
 */
public enum DataSourceType {
    /**
     * 쓰기 작업용 Primary 데이터소스
     */
    PRIMARY,

    /**
     * 읽기 작업용 Replica 데이터소스
     */
    REPLICA
}

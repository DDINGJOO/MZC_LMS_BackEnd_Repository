package com.mzc.backend.lms.common.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Read/Write 분리를 위한 동적 데이터소스 라우팅
 *
 * Transaction의 readOnly 속성에 따라 적절한 데이터소스를 선택합니다.
 * - readOnly = true: REPLICA (Slave DB)
 * - readOnly = false 또는 미지정: PRIMARY (Master DB)
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 현재 트랜잭션의 readOnly 속성을 확인하여 데이터소스 키를 결정
     *
     * @return DataSourceType.REPLICA (readOnly = true) 또는 DataSourceType.PRIMARY (readOnly = false)
     */
    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        return isReadOnly ? DataSourceType.REPLICA : DataSourceType.PRIMARY;
    }
}

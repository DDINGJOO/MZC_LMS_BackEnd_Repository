package com.mzc.backend.lms.common.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Multi-DataSource 구성
 *
 * spring.datasource.routing.enabled=true 일 때만 활성화됩니다.
 * Primary와 Replica 데이터소스를 구성하고, 트랜잭션 readOnly 속성에 따라
 * 적절한 데이터소스로 라우팅합니다.
 *
 * LazyConnectionDataSourceProxy를 통해 실제 커넥션이 필요한 시점까지
 * 데이터소스 결정을 지연시켜 트랜잭션 readOnly 속성이 올바르게 반영되도록 합니다.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.datasource.routing.enabled", havingValue = "true")
public class DataSourceConfig {

    /**
     * Primary(Master) 데이터소스 생성
     * - 쓰기(INSERT, UPDATE, DELETE) 작업 담당
     *
     * @return Primary DataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary.hikari")
    public DataSource primaryDataSource() {
        log.info("Configuring Primary DataSource (Master)");
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * Replica(Slave) 데이터소스 생성
     * - 읽기(SELECT) 작업 담당
     * - HikariCP 설정에서 read-only: true로 설정 권장
     *
     * @return Replica DataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.replica.hikari")
    public DataSource replicaDataSource() {
        log.info("Configuring Replica DataSource (Slave)");
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 라우팅 데이터소스 구성
     * - Primary와 Replica 데이터소스를 맵에 등록
     * - 기본 데이터소스는 Primary로 설정
     *
     * @return RoutingDataSource
     */
    @Bean
    public DataSource routingDataSource(DataSource primaryDataSource, DataSource replicaDataSource) {
        log.info("Configuring RoutingDataSource for Read/Write separation");

        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.PRIMARY, primaryDataSource);
        dataSourceMap.put(DataSourceType.REPLICA, replicaDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource);

        return routingDataSource;
    }

    /**
     * LazyConnectionDataSourceProxy로 래핑된 최종 데이터소스
     *
     * LazyConnectionDataSourceProxy를 사용하는 이유:
     * 1. 트랜잭션 시작 시점에는 readOnly 속성이 아직 설정되지 않음
     * 2. 실제 커넥션이 필요한 시점까지 데이터소스 선택을 지연
     * 3. 이를 통해 @Transactional(readOnly=true)이 올바르게 적용됨
     *
     * @return LazyConnectionDataSourceProxy wrapping RoutingDataSource
     */
    @Primary
    @Bean
    public DataSource dataSource(DataSource routingDataSource) {
        log.info("Configuring LazyConnectionDataSourceProxy for deferred DataSource determination");
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}
